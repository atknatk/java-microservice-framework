package com.esys.framework.core.security.field;

import com.esys.framework.core.security.field.policy.EvalulationLogic;
import net.bytebuddy.implementation.attribute.AnnotationRetention;
import org.jboss.jandex.AnnotationTarget;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SecureField{

        Class[] policyClasses() default {};

        String[] policyBeans() default {};

        EvalulationLogic policyLogic() default EvalulationLogic.AND;

        String[] roles() default {};

        String[] authorities() default {};

        EvalulationLogic roleLogic() default EvalulationLogic.AND;

        EvalulationLogic authrorityLogic() default EvalulationLogic.AND;

        /**
         * JSON'da field'ın değerini override eder
         */
        String overrideMessage() default "***";

        /**
         * JSON'da field hiç gösterilmemesi için true ile güncellenmelidir
         */
        boolean ignoreField() default false;
}
