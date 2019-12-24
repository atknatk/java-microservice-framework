package com.esys.framework.uaa.web.controller;

import com.codahale.metrics.annotation.Timed;
import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.dto.uaa.UserDto;
import com.esys.framework.core.entity.uaa.Authority;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.uaa.captcha.ICaptchaService;
import com.esys.framework.uaa.registration.OnRegistrationCompleteEvent;
import com.esys.framework.uaa.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/account", produces={MediaType.APPLICATION_JSON_VALUE})
public class AccountController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private ICaptchaService captchaService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    private static String authorizationRequestBaseUri = "oauth2/authorization";
    Map<String, String> oauth2AuthenticationUrls = new HashMap<>();


    @Timed
    @ResponseBody
    @PostMapping(value = "/register", produces={MediaType.APPLICATION_JSON_VALUE})
    public ModelResult<? extends AbstractDto> registerAccount(@Valid @RequestBody UserDto userDto, HttpServletRequest request) {
        //log.debug("Registering user account with information: {}", userDto);
        //final UserDto registered = userService.registerNewUserAccount(userDto);
        //eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
        //return new ModelResult.ModelResultBuilder(messageSource).setData(userDto).build();

        //Captcha ile register iptal edilmek istenir ise üstteki kodları yorumdan kaldırınız.
        return new ModelResult.ModelResultBuilder(messageSource).setData(userDto).build();
    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public ResponseEntity<Object> confirmRegistration(final HttpServletRequest request, @RequestParam("token") final String token) throws UnsupportedEncodingException, URISyntaxException {
        Locale locale = request.getLocale();
        final String result = userService.validateVerificationToken(token);
        if (result.equals("valid")) {
            final User user = userService.getUserFromVerificationToken(token);
            authWithoutPassword(user);
//            return new ModelResult.ModelResultBuilders(messageSource)
//                    .setStatus(0)
//                    .setMessageKey("uaa.accountVerified",locale)
//                    .build();
            try {
                URI uri = new URI("http://" + request.getServerName() + ":" + request.getServerPort()+"/#/login?registrationConfirm=success");
                return ResponseEntity.status(301).location(uri).build();
            } catch (URISyntaxException e) {
                log.error("Url moved error",e);
                URI uri = new URI("http://" + request.getServerName() + ":" + request.getServerPort()+"/#/login?registrationConfirm=error");
                return ResponseEntity.status(301).location(uri).build();

            }
        }
        URI uri = new URI("http://" + request.getServerName() + ":" + request.getServerPort()+"/#/login?registrationConfirm=timeout");
        return ResponseEntity.status(301).location(uri).build();

//        return new ModelResult.ModelResultBuilders(messageSource)
//                .setMessageKey("uaa.auth.message." + result,locale)
//                .build();
    }

    @Timed
    @ResponseBody
    @PostMapping(value = "/register/captcha")
    public ModelResult captchaRegisterUserAccount(@Valid @RequestBody final UserDto accountDto, final HttpServletRequest request) {
        final String response = request.getParameter("g-recaptcha-response");
        captchaService.processResponse(response);
        log.debug("Registering user account with information: {}", accountDto);
        final UserDto registered = userService.registerNewUserAccount(accountDto);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), getAppUrl(request)));
        return new ModelResult.ModelResultBuilder(messageSource).setData(registered).build();
    }



    @GetMapping(value = "/oauth2/client", produces={MediaType.APPLICATION_JSON_VALUE})
    @Timed
    public ModelResult getOuath2Login(final HttpServletRequest request) {
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                .as(Iterable.class);
        if (type != ResolvableType.NONE &&
                ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }

        clientRegistrations.forEach(registration ->
                oauth2AuthenticationUrls.put(registration.getClientName(),
                        authorizationRequestBaseUri + "/" + registration.getRegistrationId()));

        return new ModelResult.ModelResultBuilder(messageSource).setData(oauth2AuthenticationUrls).build();
    }



    // ============== NON-API ============



    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }


    public void authWithoutPassword(User user) {
        List<Authority> authorities = user.getRoles().stream()
                .map(role -> role.getAuthorities())
                .flatMap(list -> list.stream())
                .distinct()
                .collect(Collectors.toList());

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
