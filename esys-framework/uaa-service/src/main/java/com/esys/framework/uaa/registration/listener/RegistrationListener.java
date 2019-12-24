package com.esys.framework.uaa.registration.listener;

import com.esys.framework.core.configuration.EsysProperties;
import com.esys.framework.core.dto.uaa.UserDto;
import com.esys.framework.uaa.registration.OnRegistrationCompleteEvent;
import com.esys.framework.uaa.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Slf4j
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private static final String USER = "user";
    private static final String BASE_URL = "baseUrl";


    @Autowired
    private IUserService service;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EsysProperties esysProperties;

    @Inject
    private SpringTemplateEngine templateEngine;
    // API

    @Override
    public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(final OnRegistrationCompleteEvent event) {
        final UserDto user = event.getUser();
        final String token = UUID.randomUUID().toString();
        service.createVerificationTokenForUser(user, token);

        sendActivationEmail(event, user, token);
      //  final SimpleMailMessage email = constructEmailMessage(event, user, token);
        //  mailSender.send(email);
    }

    //
//
//    private final SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event,
//                                                          final UserDto user, final String token) {
//        final String recipientAddress = user.getEmail();
//        final String subject = "Registration Confirmation";
//        final String confirmationUrl = event.getAppUrl() + "/uaa/account/registrationConfirm?token=" + token;
//        final String message = messages.getMessage("message.regSucc", null, event.getLocale());
//        final SimpleMailMessage email = new SimpleMailMessage();
//        email.setTo(recipientAddress);
//        email.setSubject(subject);
//        email.setText(message + " \r\n" + confirmationUrl);
//        email.setFrom(env.getProperty("support.email"));
//        return email;
//    }


    private final void sendActivationEmail(final OnRegistrationCompleteEvent event,
                                             final UserDto user, final String token) {
        log.debug("Sending activation e-mail to '{}'", user.getEmail());
        Context context = new Context(event.getLocale());
        context.setVariable("name", "Merhaba "+user.getFirstName() + " " + user.getLastName()+ ",");
        final String confirmationUrl = event.getAppUrl() + "/uaa/account/registrationConfirm?token=" + token;
        context.setVariable("confirmationUrl", confirmationUrl);
        context.setVariable(BASE_URL, event.getAppUrl());
        String content = templateEngine.process("activation", context);
        String subject = messages.getMessage("email.activation.title", null, event.getLocale());
        sendEmail(user.getEmail(), subject.equals("") ? "ESYS'ye Hoşgeldiniz" : subject, content, false, true);
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(esysProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            mailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
        }
    }

}
