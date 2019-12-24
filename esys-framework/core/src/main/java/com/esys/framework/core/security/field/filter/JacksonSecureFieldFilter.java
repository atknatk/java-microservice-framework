package com.esys.framework.core.security.field.filter;

import com.esys.framework.core.security.field.SecureField;
import com.esys.framework.core.security.field.entity.EntityCreatedByProvider;
import com.esys.framework.core.security.field.exceptions.AccessDeniedExceptionHandler;
import com.esys.framework.core.security.field.policy.ContextAwareFieldSecurityPolicy;
import com.esys.framework.core.security.field.policy.EvalulationLogic;
import com.esys.framework.core.security.field.policy.FieldSecurityPolicy;
import com.esys.framework.core.security.field.principal.PrincipalProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JacksonSecureFieldFilter extends SimpleBeanPropertyFilter {
    @Autowired
    private final ApplicationContext applicationContext;

    @Autowired
    private final PrincipalProvider globalPrincipalProvider;

    @Autowired
    private final EntityCreatedByProvider globalEntityCreatedByProvider;

    @Autowired
    private final AccessDeniedExceptionHandler accessDeniedExceptionHandler;


    public JacksonSecureFieldFilter(final ApplicationContext applicationContext,
                                    final PrincipalProvider globalPrincipalProvider,
                                    final EntityCreatedByProvider globalEntityCreatedByProvider,
                                    final AccessDeniedExceptionHandler accessDeniedExceptionHandler){
        this.applicationContext = applicationContext;
        this.globalPrincipalProvider = globalPrincipalProvider;
        this.globalEntityCreatedByProvider = globalEntityCreatedByProvider;
        this.accessDeniedExceptionHandler = accessDeniedExceptionHandler;

    }



    @Override
    public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer) throws Exception {
            SecureField secureField = writer.findAnnotation(SecureField.class);

            boolean permit;

            if(secureField != null){
                 permit = executePolicies(secureField,writer,pojo);
            }else{
                permit = true;
            }

        if (permit){
            log.debug("Field Permitted: {}",writer.getName());
            writer.serializeAsField(pojo, jgen, provider);
        } else {
            log.debug("Field Denied: {}",writer.getName());
                if(secureField.ignoreField()) return;
            jgen.writeObjectField(writer.getName(), secureField.overrideMessage());
        }
    }

    /**
     * SecureField üzerindeki tüm policy ve bean'ler çalıştırılır.
     * @param secureField
     * @param writer
     * @param pojo
     * @return
     */
    private boolean executePolicies(SecureField secureField, PropertyWriter writer, Object pojo) {
        List<FieldSecurityPolicy> policies  = null;
        try {
            policies = getPolicies(secureField);
        } catch (InstantiationException e) {
            log.error("Policy is null", e);
        } catch (IllegalAccessException e) {
            log.error("Policy is null", e);
        }

        if(policies == null){
            return false;
        }

        log.debug("Executing policies for field {}.[{}] annotated by @SecureField={}"
                ,writer.getMember().getDeclaringClass(),
                writer.getMember().getName(),
                secureField);

        int i = 0;
        for(FieldSecurityPolicy policy : policies){
            try {
                if(runPolicy(policy,secureField,writer,pojo))
                    i++;
            } catch (Exception e) {
                log.error("",e);
            }
        }


        if(secureField.roleLogic() == EvalulationLogic.AND){
            return i == policies.size();
        }

        if(secureField.roleLogic() == EvalulationLogic.OR){
            return i > 0;
        }

        if(secureField.roleLogic() == EvalulationLogic.XOR){
            return i == 1;
        }
        return false;
    }

    /**
     * SecureField üzerindeki policy'ler toplanır.
     * @param secureField
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @NotNull
    private List<FieldSecurityPolicy> getPolicies(SecureField secureField) throws InstantiationException, IllegalAccessException {
        Class[] policyClasses = secureField.policyClasses();
        List<FieldSecurityPolicy> policyBeans =
                Arrays.stream(secureField.policyBeans())
                        .map(b -> applicationContext.getBean(b,FieldSecurityPolicy.class)).collect(Collectors.toList());

//        val policyBeans = Arrays.stream(secureField.policyBeans()).map {
//            applicationContext.getBean(it, FieldSecurityPolicy::class) as FieldSecurityPolicy
//        }.toMutableList()

        if( secureField.roles().length > 0 ){
            policyBeans.add(getPolicyBean("roleBasedFieldSecurityPolicy"));
        }

        if( secureField.authorities().length > 0 ){
            policyBeans.add(getPolicyBean("authorityBasedFieldSecurityPolicy"));
        }

        // default
        if( policyBeans.isEmpty() && policyClasses.length == 0 ){
            policyBeans.add(getPolicyBean("createdByFieldSecurityPolicy"));
        }

        // together
        List<FieldSecurityPolicy> policies = new ArrayList<>();
        policies.addAll(new ArrayList<>(policyBeans));

        for(Class policyClass : policyClasses){
            policies.addAll(new ArrayList<>(Arrays.asList(initPolicyClass(policyClass))));
        }

        return policies;
    }

    @NotNull
    private boolean runPolicy(FieldSecurityPolicy policy,SecureField secureField,PropertyWriter writer,Object pojo) throws Exception {
        try{
           return policy.permitAccess(secureField,
                    writer,
                    pojo,
                    globalEntityCreatedByProvider.getCreatedBy(pojo),
                    globalPrincipalProvider.getPrincipal());

        }catch (Exception ex){
            log.debug("Exception during policy: {}",ex.getMessage());
          return   accessDeniedExceptionHandler.permitAccess(ex);
        }
    }


    @NotNull
    private FieldSecurityPolicy getPolicyBean(String policyBeanName){
        return applicationContext.getBean(policyBeanName, FieldSecurityPolicy.class);
    }

    @NotNull
    private FieldSecurityPolicy initPolicyClass(@NotNull Class<FieldSecurityPolicy> policy) throws IllegalAccessException, InstantiationException {
        FieldSecurityPolicy policyInstance = policy.newInstance();
        if ( policyInstance instanceof ContextAwareFieldSecurityPolicy){
            ((ContextAwareFieldSecurityPolicy)policyInstance).setApplicationContext(applicationContext);
        }
        return policyInstance;
    }


}
