package com.esys.framework.core.model;


import com.esys.framework.core.consts.FieldSecurityConstants;
import com.esys.framework.core.consts.ResultStatusCode;
import com.esys.framework.core.enums.MessageType;
import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@JsonFilter(FieldSecurityConstants.SECURITY_FIELD_FILTER)
@Slf4j
public class ModelResult<T> {


    @Getter
    @Setter
    private int status = -1;

    @Getter
    private T data;

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private String messageKey;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private String titleKey;

    @Getter
    @Setter
    private Long objectId;

    @Getter
    @Setter
    private List<String> errors;

    @Getter
    @Setter
    private MessageType messageType;


    public boolean isSuccess() {
        return status == ResultStatusCode.SUCCESS;
    }

    public void setData(T data) {
       if(data != null){
           this.status = ResultStatusCode.SUCCESS;
           this.message = "success";
       }
       this.data = data;
    }


    public ModelResult() {
    }

    private ModelResult(ModelResultBuilder<T> builder) {
        this.status=builder.status;
        this.data=builder.data;
        this.message=builder.message;
        this.messageKey=builder.messageKey;
        this.title=builder.title;
        this.titleKey=builder.titleKey;
        this.errors=builder.errors;
        this.objectId = builder.objectId;
        this.messageType = builder.messageType;
    }

    private ModelResult(ModelResultBuilders builder) {
        this.status=builder.status;
        this.message=builder.message;
        this.messageKey=builder.messageKey;
        this.title=builder.title;
        this.titleKey=builder.titleKey;
        this.errors=builder.errors;
        this.objectId = builder.objectId;
        this.messageType = builder.messageType;
    }

    public static class ModelResultBuilder<T>{
        private MessageSource messageSource;
        private Locale locale;

        public static ModelResultBuilder getNew() {
            return new ModelResultBuilder();
        }

        public ModelResultBuilder(MessageSource messageSource) {
            this.messageSource = messageSource;
            this.locale = LocaleContextHolder.getLocale();

        }

        public ModelResultBuilder(MessageSource messageSource,Locale locale) {
            this.messageSource = messageSource;
            this.locale = locale;
        }

        public ModelResultBuilder() {
        }

        // required parameters
        private int status = -1;
        private String message;
        private String messageKey;
        private String title;
        private String titleKey;
        private MessageType messageType;


        // optional parameters
        private T data;
        private List<String> errors;
        private Long objectId;


        public  ModelResultBuilder setStatus(int status) {
            this.status = status;
            return this;
        }

        public ModelResultBuilder setData(T data) {
            this.status = ResultStatusCode.SUCCESS;
            this.messageType = MessageType.SUCCESS;
            this.data = data;
            return this;
        }

        public ModelResultBuilder setData(T data, Locale locale) {
            this.setMessageWithLocale(locale);
            this.status = ResultStatusCode.SUCCESS;
            this.messageType = MessageType.SUCCESS;
            this.data = data;
            return this;
        }

        public ModelResultBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public ModelResultBuilder setMessageKey(String messageKey) {
            this.messageKey = messageKey;
            return this;
        }

        public ModelResultBuilder setMessageKey(String messageKey, Locale locale) {
            this.setMessageWithLocale(locale);
            this.messageKey = messageKey;
            return this;
        }

        public ModelResultBuilder setTitle(String title) {
            this.title = title;
            return this;
        }


        public ModelResultBuilder setTitleKey(String titleKey) {
            this.titleKey = titleKey;
            return this;
        }

        public ModelResultBuilder setTitleKey(String titleKey, Locale locale) {
            this.setTitleWithLocale(locale);
            this.titleKey = titleKey;
            return this;
        }

        public ModelResultBuilder setObjectId(Long objectId) {
            this.objectId = objectId;
            this.status = ResultStatusCode.SUCCESS;
            return this;
        }

        public ModelResultBuilder setErrors(List<String> errors) {
            this.errors = errors;
            return this;
        }

        public ModelResultBuilder addError(String error) {
            if(errors == null){
                errors = new ArrayList<String>();
            }
            errors.add(error);
            return this;
        }

        public ModelResult success() {
            this.status = ResultStatusCode.SUCCESS;
            setMessageKey("success",locale != null ? locale : LocaleContextHolder.getLocale());
            this.messageType = MessageType.SUCCESS;
            return build();
        }

        public ModelResultBuilder setMessageType(MessageType messageType) {
            this.messageType = messageType;
            return this;
        }

        private void setMessageWithLocale(Locale locale){
            if(messageSource != null){
                if(locale != null){
                    this.message = messageSource.getMessage(messageKey,null,locale);
                }else if(this.locale != null){
                    this.message = messageSource.getMessage(messageKey,null,this.locale);
                }else{
                    log.warn("Locale is null MessageKey : {}", this.messageKey);
                }
            }
        }

        private void setTitleWithLocale(Locale locale){
            if(messageSource != null){
                if(locale != null){
                    this.title = messageSource.getMessage(titleKey,null,locale);
                }else if(this.locale != null){
                    this.title = messageSource.getMessage(titleKey,null,this.locale);
                }else{
                    log.warn("Locale is null TitleKey : {}", this.titleKey);
                }
            }
        }


        public ModelResult<T> build(){
            if(StringUtils.isNotEmpty(this.messageKey) && StringUtils.isEmpty(this.titleKey)){
                this.titleKey = this.messageKey+ ".title";
                this.setTitleWithLocale(locale);
            }
            return new ModelResult<T>(this);
        }

    }

    public static class ModelResultBuilders{

        private MessageSource messageSource;
        private Locale locale;

        public static ModelResultBuilders getNew() {
            return new ModelResultBuilders();
        }

        public static ModelResultBuilders getNew(MessageSource messageSource) {
            return new ModelResultBuilders(messageSource);
        }

        public static ModelResultBuilders getNew(MessageSource messageSource, Locale locale) {
            return new ModelResultBuilders(messageSource, locale);
        }



        public ModelResultBuilders(MessageSource messageSource) {
            this.messageSource = messageSource;
            this.locale = LocaleContextHolder.getLocale();
        }

        public ModelResultBuilders(MessageSource messageSource,Locale locale) {
            this.messageSource = messageSource;
            this.locale = locale;
        }

        public ModelResultBuilders() {
        }

        // required parameters
        private int status = -1;
        private String message;
        private String messageKey;
        private List<String> errors;
        private String title;
        private String titleKey;

        private Long objectId;
        private MessageType messageType;

        public ModelResultBuilders setStatus(int status) {
            this.status = status;
            return this;
        }

        public ModelResultBuilders setSuccess() {
            this.status = ResultStatusCode.SUCCESS;
            this.messageType = MessageType.SUCCESS;
            return this;
        }

        /**
         * Message Key : success
         * Status : ResultStatusCode.SUCCESS;
         * @return ModelResult ModelResult doner
         */
        public ModelResult success() {
            this.messageType = MessageType.SUCCESS;
            this.status = ResultStatusCode.SUCCESS;
            setMessageKey("success",locale == null ? LocaleContextHolder.getLocale() : locale);
            return new ModelResult(this);
        }

        public ModelResultBuilders setMessageKey(String messageKey, Locale locale) {
            this.messageKey = messageKey;
            this.setMessageWithLocale(locale);
            return this;
        }

        public ModelResultBuilders setMessageKey(String messageKey) {
            this.messageKey = messageKey;
            this.setMessageWithLocale(LocaleContextHolder.getLocale());
            return this;
        }

        public ModelResultBuilders setMessage(String message) {
            this.message = message;
            return this;
        }


        public ModelResultBuilders setErrors(List<String> errors) {
            this.errors = errors;
            return this;
        }

        public ModelResultBuilders setObjectId(Long objectId) {
            this.objectId = objectId;
            this.status = ResultStatusCode.SUCCESS;
            this.messageType = MessageType.SUCCESS;
            return this;
        }

        public ModelResultBuilders addError(String error) {
            if(errors == null){
                errors = new ArrayList<String>();
            }
            errors.add(error);
            return this;
        }

        public ModelResultBuilders setTitle(String title) {
            this.title = title;
            return this;
        }

        public ModelResultBuilders setTitleKey(String titleKey) {
            this.titleKey = titleKey;
            return this;
        }


        public ModelResultBuilders setTitleKey(String titleKey, Locale locale) {
            this.setTitleWithLocale(locale);
            this.titleKey = titleKey;
            return this;
        }

        public ModelResultBuilders setMessageType(MessageType messageType) {
            this.messageType = messageType;
            return this;
        }



        public ModelResult build(){
            if(StringUtils.isNotEmpty(this.messageKey) && StringUtils.isEmpty(this.titleKey)){
                this.titleKey = this.messageKey+ ".title";
                this.setTitleWithLocale(locale);
            }
            return new ModelResult(this);
        }

        private void setMessageWithLocale(Locale locale){
            if(messageSource != null){
                if(locale != null){
                    this.message = messageSource.getMessage(messageKey,null,locale);
                }else if(this.locale != null){
                    this.message = messageSource.getMessage(messageKey,null,this.locale);
                }else{
                    log.warn("Locale is null MessageKey : {}", this.messageKey);
                }
            }
        }
        private void setTitleWithLocale(Locale locale){
            if(messageSource != null){
                if(locale != null){
                    this.title = messageSource.getMessage(titleKey,null,locale);
                }else if(this.locale != null){
                    this.title = messageSource.getMessage(titleKey,null,this.locale);
                }else{
                    log.warn("Locale is null TitleKey : {}", this.titleKey);
                }
            }
        }

    }

}
