package com.esys.framework.core.exceptions;

import brave.Tracer;
import brave.Tracing;
import com.esys.framework.core.consts.ResultStatusCode;
import com.esys.framework.core.entity.core.LogEvent;
import com.esys.framework.core.entity.core.LogException;
import com.esys.framework.core.enums.MessageType;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.core.security.SecurityUtils;
import com.esys.framework.core.service.ILogExceptionService;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    MessageSource messageSource;

    @Autowired
    ILogExceptionService exceptionService;

    @Value("${spring.application.name}")
    private String appName;


    @ExceptionHandler(AllreadyExistsException.class)
    public ResponseEntity<?> allreadyExistsException(AllreadyExistsException ex, WebRequest request) {
        ModelResult.ModelResultBuilders builders = new ModelResult.ModelResultBuilders(messageSource,request.getLocale());
        if(!Strings.isNullOrEmpty(ex.getMessage())){
            builders.setMessageKey(ex.getMessage());
        }
        builders.setStatus(ResultStatusCode.ALLREADY_EXISTS);
        saveLog(ex);
        return new ResponseEntity<>(builders.build(), BAD_REQUEST);
    }

    @ExceptionHandler(NullDtoException.class)
    public ResponseEntity<?> allreadyExistsException(NullDtoException ex, WebRequest request) {
        ModelResult.ModelResultBuilders builders = new ModelResult.ModelResultBuilders(messageSource,request.getLocale());
        if(!Strings.isNullOrEmpty(ex.getMessage())){
            builders.setMessageKey(ex.getMessage());
        }
        builders.setStatus(ResultStatusCode.DTO_NOT_VALID);
        saveLog(ex);
        return new ResponseEntity<>(builders.build(), BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ModelResult result = new ModelResult.ModelResultBuilders(messageSource,request.getLocale())
                .setMessageKey(ex.getMessage())
                .setStatus(ResultStatusCode.RESOURCE_NOT_FOUND)
                .build();
        saveLog(ex);
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> accessDeniedException(AccessDeniedException ex, WebRequest request) {

        ModelResult result = new ModelResult.ModelResultBuilders(messageSource,request.getLocale())
                .setMessageKey("forbidden")
                .setStatus(ResultStatusCode.FORBIDDEN)
                .build();
        saveLog(ex);
        return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler({ ReCaptchaUnavailableException.class })
    public ResponseEntity<Object> handleReCaptchaUnavailable(final RuntimeException ex, final WebRequest request) {
        log.error("500 Status Code", ex);
        ModelResult result = new ModelResult.ModelResultBuilders(messageSource,request.getLocale())
                .setMessageKey(ex.getMessage())
                .setStatus(ResultStatusCode.RECAPTCHA_UNAVAILABLE)
                .build();
        saveLog(ex);
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ ReCaptchaInvalidException.class })
    public ResponseEntity<?> handleReCaptchaInvalid(final RuntimeException ex, final WebRequest request) {
        log.error("400 Status Code", ex);
        ModelResult result = new ModelResult.ModelResultBuilders(messageSource,request.getLocale())
                .setMessageKey(ex.getMessage())
                .setStatus(ResultStatusCode.RECAPTCHA_UNAVAILABLE)
                .build();
        saveLog(ex);
        return new ResponseEntity<>(result, BAD_REQUEST);
    }

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelResult methodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        saveLog(ex);
        return processFieldErrors(fieldErrors,request);
    }
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> invalidTokenException(InvalidTokenException ex, WebRequest request) {
        ModelResult result = new ModelResult.ModelResultBuilders(messageSource,request.getLocale())
                .setMessageKey(ex.getOAuth2ErrorCode())
                .setStatus(ResultStatusCode.INVALID_TOKEN)
                .build();
        saveLog(ex);
        return new ResponseEntity<>(result, UNAUTHORIZED);
    }

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<?> methodArgumentNotValidException(IncorrectPasswordException ex, WebRequest request) {
        log.warn("INCORRECT_PASSWORD User : {}" , SecurityUtils.getCurrentUserLogin());
        ModelResult result = new ModelResult.ModelResultBuilders(messageSource,request.getLocale())
                .setMessageKey(ex.getMessage())
                .setStatus(ResultStatusCode.INCORRECT_PASSWORD)
                .setMessageType(MessageType.ERROR)
                .build();
        saveLog(ex);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserGroupException.class)
    public ResponseEntity<?> methodArgumentNotValidException(UserGroupException ex, WebRequest request) {
        log.warn("USERGROUP_NOT_ACTIVE : {}" , SecurityUtils.getCurrentUserLogin());
        ModelResult result = new ModelResult.ModelResultBuilders(messageSource,request.getLocale())
                .setMessageKey(ex.getMessage())
                .setStatus(ResultStatusCode.USERGROUP_NOT_ACTIVE)
                .build();
        saveLog(ex);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
        ModelResult result = new ModelResult.ModelResultBuilders()
                .setMessageKey(ex.getMessage())
                .setMessage(ex.getLocalizedMessage())
                .setStatus(ResultStatusCode.UNKNOWN_ERROR)
                .build();
        saveLog(ex);
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ModelResult processFieldErrors(List<org.springframework.validation.FieldError> fieldErrors, WebRequest request) {
        ModelResult.ModelResultBuilders builders = new ModelResult.ModelResultBuilders(messageSource,request.getLocale());
        for (org.springframework.validation.FieldError fieldError: fieldErrors) {
            builders.setMessageKey(fieldError.getDefaultMessage());
            builders.addError(fieldError.getField()+"|"+fieldError.getDefaultMessage());
        }
        return builders.setStatus(ResultStatusCode.DTO_NOT_VALID).build();
    }


    @Async
    public void saveLog(Exception ex){
        LogException exception = getLogEx();
        exception.setException(ex.getMessage());
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter( writer );
        ex.printStackTrace( printWriter );
        printWriter.flush();
        exception.setException(writer.toString());
        exceptionService.saveLog(exception);
    }


    private LogException getLogEx(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String userAgent = request.getHeader("User-Agent");
        LogException ex = new LogException();
        ex.setUserAgent(userAgent);
        ex.setOS(getOS(userAgent));
        ex.setBrowser(getBrowser(userAgent));
        ex.setClient(getOAuthTokenScopes().iterator().next());
        ex.setIp(request.getRemoteAddr());
        ex.setModule(appName);
        return ex;
    }


    private Set<String> getOAuthTokenScopes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2Authentication oAuth2Authentication;

        if (authentication instanceof OAuth2Authentication) {
            oAuth2Authentication = (OAuth2Authentication) authentication;
        } else {
            throw new IllegalStateException("Authentication not supported!");
        }

        return oAuth2Authentication.getOAuth2Request().getScope();
    }

    private String getBrowser(String userAgent){
        String browser = "";
        String  user            =   userAgent.toLowerCase();
        if (user.contains("msie"))
        {
            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
        } else if (user.contains("safari") && user.contains("version"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
        } else if ( user.contains("opr") || user.contains("opera"))
        {
            if(user.contains("opera"))
                browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            else if(user.contains("opr"))
                browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
        } else if (user.contains("chrome"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)  || (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1) )
        {
            //browser=(userAgent.substring(userAgent.indexOf("MSIE")).split(" ")[0]).replace("/", "-");
            browser = "Netscape-?";

        } else if (user.contains("firefox"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } else if(user.contains("rv"))
        {
            browser="IE-" + user.substring(user.indexOf("rv") + 3, user.indexOf(")"));
        } else
        {
            browser = "UnKnown, More-Info: "+userAgent;
        }
        return browser;
    }
    private String getOS(String userAgent){
        String os = "";
        if (userAgent.toLowerCase().indexOf("windows") >= 0 )
        {
            os = "Windows";
        } else if(userAgent.toLowerCase().indexOf("mac") >= 0)
        {
            os = "Mac";
        } else if(userAgent.toLowerCase().indexOf("x11") >= 0)
        {
            os = "Unix";
        } else if(userAgent.toLowerCase().indexOf("android") >= 0)
        {
            os = "Android";
        } else if(userAgent.toLowerCase().indexOf("iphone") >= 0)
        {
            os = "IPhone";
        }else{
            os = "UnKnown, More-Info: "+userAgent;
        }
        return os;
    }


}
