package com.esys.framework.base.mail.listener;

import com.esys.framework.base.mail.OnMailSendEvent;
import com.esys.framework.core.configuration.EsysProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;

@Slf4j
@Component
public class MailSendListener implements ApplicationListener<OnMailSendEvent> {

    private static final String USER = "user";
    private static final String BASE_URL = "baseUrl";



    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EsysProperties esysProperties;

    @Inject
    private SpringTemplateEngine templateEngine;
    // API

    private final void sendActivationEmail(final OnMailSendEvent event) {
        // log.debug("Sending e-mail to '{}'", user.getEmail());
        Context context = new Context(LocaleContextHolder.getLocale());
        context.setVariable("content",event.getContent());
        String content = templateEngine.process("mail_template", context);
        sendEmail(event.getToMail(), event.getSubject(), content, false, true);
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

    @Override
    public void onApplicationEvent(OnMailSendEvent event) {
        sendActivationEmail(event);
    }
}
