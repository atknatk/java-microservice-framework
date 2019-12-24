package com.esys.framework.base.mail;

import com.esys.framework.core.dto.uaa.BasicUserDto;
import com.esys.framework.core.dto.uaa.UserDto;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@SuppressWarnings("serial")
public class OnMailSendEvent extends ApplicationEvent {

    private final BasicUserDto user;
    private final String toMail;
    private final String subject;
    private final String content;

    public OnMailSendEvent(final BasicUserDto user, final String toMail, final String subject, final String content) {
        super(user);
        this.user = user;
        this.toMail = toMail;
        this.subject = subject;
        this.content = content;
    }


    public BasicUserDto getUser() {
        return user;
    }

    public String getToMail() {
        return toMail;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }
}
