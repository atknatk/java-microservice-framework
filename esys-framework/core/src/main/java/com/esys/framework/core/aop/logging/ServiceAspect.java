package com.esys.framework.core.aop.logging;

import brave.Tracer;
import brave.Tracing;
import com.esys.framework.core.consts.Constants;
import com.esys.framework.core.entity.core.LogEvent;
import com.esys.framework.core.repository.ILogEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

@Aspect
@Component
@Slf4j
public class ServiceAspect {

    @Inject
    private Environment env;

    @Inject
    private ILogEventRepository repository;

    @Value("${spring.application.name}")
    private String appName;

    ObjectMapper mapper = new ObjectMapper();

    @Pointcut("within(com.esys.framework..service..*) ")
    public void loggingPointcut() {
    }

    @AfterThrowing(pointcut = "loggingPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        if (env.acceptsProfiles(Constants.SPRING_PROFILE_DEVELOPMENT)) {
            log.error("Exception in {}.{}() with cause = {} and exception {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), e.getCause(), e);
        } else {
            log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), e.getCause());
        }
    }

    @Around("loggingPointcut() && !@annotation(com.esys.framework.core.aop.logging.NoLogging)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }

        LogEvent event;
        try {
            event = getLogEvent();
        }catch (Exception ex){
            event = new LogEvent();
        }

        try {
            long startTime = new Date().getTime();
            Object result = joinPoint.proceed();
            long endTime = new Date().getTime();
            event.setExecutionTime(endTime-startTime);
            event.setSuccess(true);

            Signature signature = joinPoint.getSignature();
            event.setMethod(signature.getName());
            event.setService(signature.getDeclaringTypeName());
            try{
                String json = mapper.writeValueAsString(joinPoint.getArgs());
                if(json.length() > 10000){
                    event.setParameter("{\"dataLength\" : \"oversize\"}");
                }else{
                    event.setParameter(json);
                }
            }catch (Exception ex){}
            try{
                String json = mapper.writeValueAsString(result);
                if(json.length() > 10000){
                    event.setReturnValue("{\"dataLength\" : \"oversize\"}");
                }else{
                    event.setReturnValue(json);
                }

            }catch (Exception ex){}


            //event.setMethod(joinPoint.getSignature());
            saveLog(event);
            if (log.isDebugEnabled()) {
                log.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            event.setSuccess(false);
            saveLog(event);
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

            throw e;
        }
    }


    @Async
    public void saveLog(LogEvent event){
        repository.save(event);
    }


    private LogEvent getLogEvent(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        String userAgent = request.getHeader("User-Agent");
        LogEvent event = new LogEvent();
        event.setUserAgent(userAgent);
        event.setOS(getOS(userAgent));
        event.setBrowser(getBrowser(userAgent));
        event.setClient(getOAuthTokenScopes().iterator().next());
        event.setIp(request.getRemoteAddr());
        event.setModule(appName);



        Tracer tracer = Tracing.currentTracer();

//        Object trace = request.getAttribute("brave.SpanCustomizer");
//        if(trace != null){
//            SpanCustomizer customizer = (SpanCustomizer) trace;
//
//            String traceSpan = trace.toString()
//                    .replace("RealSpanCustomizer(","")
//                    .replace(")","");
//            String[] traceSpanArr =  traceSpan.split("/");
//            event.setTraceId(traceSpanArr[0]);
//            if(traceSpanArr.length == 2){
//                event.setSpanId(traceSpanArr[1]);
//            }
//        }
        event.setTraceId(tracer.currentSpan().context().traceIdString());
        return event;
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
