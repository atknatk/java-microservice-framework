package com.esys.framework.uaa.web.controller;

import com.codahale.metrics.annotation.Timed;
import com.esys.framework.core.client.UserClient;
import com.esys.framework.core.configuration.EsysProperties;
import com.esys.framework.core.consts.ResultStatusCode;
import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.dto.uaa.PasswordDto;
import com.esys.framework.core.dto.uaa.RoleDto;
import com.esys.framework.core.dto.uaa.UserDto;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.core.exceptions.ResourceNotFoundException;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.core.security.SecurityUtils;
import com.esys.framework.core.web.controller.BaseController;
import com.esys.framework.uaa.entity.VerificationToken;
import com.esys.framework.uaa.service.IUserService;
import com.esys.framework.uaa.web.error.InvalidOldPasswordException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController extends BaseController implements UserClient {


    @Autowired
    private IUserService userService;

    @Autowired
    private MessageSource messages;


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EsysProperties esysProperties;

    @Autowired
    private ModelMapper modelMapper;

    // user activation - verification

    @GetMapping("/resendRegistrationToken")
    @ResponseBody
    @Timed
    public ModelResult resendRegistrationToken(final HttpServletRequest request, @RequestParam("token") final String existingToken) {
        final VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
        final User user = userService.getUserFromVerificationToken(newToken.getToken());
        mailSender.send(constructResendVerificationTokenEmail(getAppUrl(request), request.getLocale(), newToken, user));
        return new ModelResult.ModelResultBuilders()
                .setMessageKey("uaa.resendToken",  request.getLocale())
                .setStatus(0)
                .build();
    }

    // Reset password
    @PostMapping("/resetPassword")
    @ResponseBody
    @Timed
    public ModelResult resetPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail) {
        final User user = userService.getUserByEmail(userEmail);
        if (user != null) {
            final String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            mailSender.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));
        }
        return new ModelResult.ModelResultBuilders()
                .setMessageKey("uaa.resetPasswordEmail",  request.getLocale())
                .setStatus(0)
                .build();
    }


    @GetMapping("/changePassword")
    @Timed
    public ModelResult showChangePasswordPage(final Locale locale, final Model model, @RequestParam("id") final long id, @RequestParam("token") final String token) {
        final String result = userService.validatePasswordResetToken(id, token);
        if (result != null) {
            model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
            return new ModelResult.ModelResultBuilders()
                    .setMessageKey("uaa.auth.message." + result,locale)
                    .build();
        }
        return new ModelResult.ModelResultBuilders().success();
    }

    @PostMapping("/savePassword")
    @ResponseBody
    public ModelResult savePassword(final Locale locale, @Valid PasswordDto passwordDto) {
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.changeUserPassword(user, passwordDto.getNewPassword());
        return new ModelResult.ModelResultBuilders()
                .setMessageKey("uaa.resetPasswordSuc",locale)
                .success();
    }

    // change user password
    @PostMapping("/updatePassword")
    @ResponseBody
    public ModelResult changeUserPassword(final Locale locale, @Valid PasswordDto passwordDto) {
        final User user = userService.getUserByEmail(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
        if (!userService.checkIfValidOldPassword(user, passwordDto.getOldPassword())) {
            throw new InvalidOldPasswordException();
        }
        userService.changeUserPassword(user, passwordDto.getNewPassword());
        return new ModelResult.ModelResultBuilders()
                .setMessageKey("uaa.updatePasswordSuc",locale)
                .success();
    }

    @GetMapping("/me")
    public ModelResult me(Principal principal) {
        User user = userService.getUserByEmailAndDomain(principal.getName(),"esys");
        UserDto userDto = modelMapper.map(user, UserDto.class);
        ModelResult.ModelResultBuilder builder = new ModelResult.ModelResultBuilder(messages);
        return builder.setData(userDto).setStatus(ResultStatusCode.SUCCESS).build();
    }

    @GetMapping("/session")
    public ModelResult<UserDto> session(String token) {
        User user = userService.getUserByEmailAndDomain(SecurityUtils.getCurrentUserLogin(),"esys");
        UserDto userDto = modelMapper.map(user, UserDto.class);
        ModelResult.ModelResultBuilder builder = new ModelResult.ModelResultBuilder(messages);
        return builder.setData(userDto).setStatus(ResultStatusCode.SUCCESS).build();
    }


    // assignRoles
    @PostMapping("/{userId}/role")
    @ResponseBody
    public ModelResult assignRoles(@PathVariable("userId") Long userId, @RequestBody List<String> roles) {
        UserDto  user = userService.getUserById(userId);
        List<RoleDto> roleCollection = new ArrayList<>();
        roles.stream().forEach(roleString -> roleCollection.add(new RoleDto(roleString)));
        user.setRoles(roleCollection);
        userService.saveUser(user);
        return new ModelResult.ModelResultBuilders()
                .setMessageKey("uaa.updateRolesSuc",LocaleContextHolder.getLocale())
                .success();
    }

    @PostMapping("/{userId}/role/{roleId}")
    @ResponseBody
    public ModelResult assignRole(@PathVariable("userId") Long userId, @PathVariable("roleId") Long roleId) {
        return userService.assignRole(userId,roleId);
    }

    @DeleteMapping("/{userId}/role/{roleId}")
    @ResponseBody
    public ModelResult removeRole(@PathVariable("userId") Long userId, @PathVariable("roleId") Long roleId) {
        return userService.removeRole(userId,roleId);
    }


    @GetMapping()
    public ModelResult<List<UserDto>> getAllUser() {
        ModelResult<List<UserDto>> result  = new ModelResult.ModelResultBuilder()
                .setData(userService.findAll(), LocaleContextHolder.getLocale())
                .build();
        return  result;
    }

    @GetMapping("/basic")
    public ModelResult<List<BasicUserDto>> getAllUserBasic() {
        ModelResult<List<BasicUserDto>> result  = new ModelResult.ModelResultBuilder()
                .setData(userService.findAllBasic(), LocaleContextHolder.getLocale())
                .build();
        return  result;
    }


    @DeleteMapping("/{userId}")
    public ModelResult<UserDto> deleteUser(final Locale locale, @PathVariable("userId") Long userId){
        UserDto userDto =  userService.deleteUser(userId);
        return new ModelResult.ModelResultBuilder()
                .setData(userDto,locale)
                .build();

    }


    @PutMapping("/{userId}")
    @ResponseBody
    public ModelResult<UserDto> updateUser(final Locale locale, @PathVariable("userId") Long userId,
                                           @RequestBody UserDto userDto) {
        if(userId == null || userId == 0){
            throw new ResourceNotFoundException("userId");
        }
        if(userDto == null){
            throw new ResourceNotFoundException("userDto");
        }

        UserDto user = userService.updateUser(userDto);
        return new ModelResult.ModelResultBuilder()
                .setData(user,locale)
                .build();
    }


    @GetMapping("/{userId}")
    public ResponseEntity<ModelResult<UserDto>> getUserById(final Locale locale, @PathVariable("userId") Long userId) {
        UserDto userDto =  userService.getUserById(userId);
        return getResponseWithData(new ModelResult.ModelResultBuilder<UserDto>()
                .setData(userDto,locale)
                .build(),HttpStatus.OK);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.HEAD)
    public ResponseEntity exist(@PathVariable("userId") Long userId) {
        if(userService.exist(userId)){
            return new ResponseEntity(HttpStatus.OK);
        }else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Timed
    @PostMapping("/checkPassword")
    public ResponseEntity<ModelResult> checkPassword(@RequestBody UserDto user) {
        boolean result= userService.checkPassword(user.getEmail(), user.getPassword());
        ModelResult res = new ModelResult.ModelResultBuilder(messages)
                .setStatus(result ?  ResultStatusCode.SUCCESS :ResultStatusCode.INVALID_GRANT)
                .setMessageKey(result ?  "uaa.wrongPassword" : "ok")
                .setTitleKey(result ?  "uaa.wrongPasswordTitle" : "ok")
                .build();
        return ok(res);
    }

    @GetMapping("/paging/basic")
    public DataTablesOutput<BasicUserDto>  listBasicUser(@Valid DataTablesInput input) {
        return userService.usersPagingBasic(input);
    }


    @RequestMapping(value = "/paging", method = RequestMethod.GET)
    public DataTablesOutput<UserDto> getUsers(@Valid DataTablesInput input) {
        return userService.usersPaging(input);
    }



    // ============== NON-API ============


    private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale, final VerificationToken newToken, final User user) {
        final String confirmationUrl = contextPath + "/registrationConfirm.html?token=" + newToken.getToken();
        final String message = messages.getMessage("message.resendToken", null, locale);
        return constructEmail("Resend Registration Token", message + " \r\n" + confirmationUrl, user);
    }

    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final User user) {
        final String url = contextPath + "/user/changePassword?id=" + user.getId() + "&token=" + token;
        final String message = messages.getMessage("message.resetPassword", null, locale);
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        return getSimpleMailMessage(subject, body, user, esysProperties.getMail());
    }

    static SimpleMailMessage getSimpleMailMessage(String subject, String body, User user, EsysProperties.Mail mail) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(mail.getSupport());
        return email;
    }

    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }


}
