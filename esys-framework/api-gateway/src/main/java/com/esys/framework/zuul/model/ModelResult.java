package com.esys.framework.zuul.model;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ModelResult<T> {


    private int status = -1;
    private T data;
    private String message;
    private String messageKey;

    private List<String> errors;

    public boolean isSuccess() {
        return status == 0;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.status = 0;
        this.message = "success";
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    private ModelResult(ModelResultBuilder<T> builder) {
        this.status=builder.status;
        this.data=builder.data;
        this.message=builder.message;
        this.messageKey=builder.messageKey;
        this.errors=builder.errors;
    }

    private ModelResult(ModelResultBuilders builder) {
        this.status=builder.status;
        this.message=builder.message;
        this.messageKey=builder.messageKey;
        this.errors=builder.errors;
    }

    public static class ModelResultBuilder<T>{
        @Autowired
        private MessageSource messageSource;

        // required parameters
        private int status = -1;
        private String message;
        private String messageKey;


        // optional parameters
        private T data;
        private List<String> errors;


        public  ModelResultBuilder setStatus(int status) {
            this.status = status;
            return this;
        }

        public ModelResultBuilder setData(T data) {
            this.status = 0;
            this.data = data;
            return this;
        }

        public ModelResultBuilder setData(T data, Locale locale) {
            this.status = 0;
            this.data = data;
            this.messageKey = "success";
            //    this.message = messageSource.getMessage(messageKey,null,locale);
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
            //      this.message = messageSource.getMessage(messageKey,null,locale);
            this.messageKey = messageKey;
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


        public ModelResult<T> build(){
            return new ModelResult<T>(this);
        }

    }

    public static class ModelResultBuilders{

        @Autowired
        private MessageSource messageSource;

        // required parameters
        private int status = -1;
        private String message;
        private String messageKey;
        private List<String> errors;


        public ModelResultBuilders setStatus(int status) {
            this.status = status;
            return this;
        }

        public ModelResultBuilders setSuccess() {
            this.status = 0;
            return this;
        }

        public ModelResult success() {
            this.status = 0;
            return new ModelResult(this);
        }

        public ModelResultBuilders setMessageKey(String messageKey, Locale locale) {
            //     this.message = messageSource.getMessage(messageKey,null,locale);
            this.messageKey = messageKey;
            return this;
        }

        public ModelResultBuilders setMessage(String message) {
            this.message = message;
            return this;
        }

        public ModelResultBuilders setMessageKey(String messageKey) {
            this.messageKey = messageKey;
            return this;
        }


        public ModelResultBuilders setErrors(List<String> errors) {
            this.errors = errors;
            return this;
        }

        public ModelResultBuilders addError(String error) {
            if(errors == null){
                errors = new ArrayList<String>();
            }
            errors.add(error);
            return this;
        }


        public ModelResult build(){
            return new ModelResult(this);
        }

    }

}
