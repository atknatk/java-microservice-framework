package com.esys.framework.core.security.field.entity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;

@Slf4j
public class SpringDataEntityCreatedByProvider implements EntityCreatedByProvider{


    @Nullable
    @Override
    public String getCreatedBy(@NotNull Object target) {
        final Field[] createdByFieldArr = {null};

        ReflectionUtils.doWithFields(target.getClass(),new ReflectionUtils.FieldCallback() {
            public final void doWith(final Field it) {
                if(it.isAnnotationPresent(CreatedBy.class)){
                    createdByFieldArr[0] = it;
                }

            }
        });

        Field createdByField = createdByFieldArr[0];
        if(createdByField != null){

            ReflectionUtils.makeAccessible(createdByField);
            if(createdByField != null ){
                try {
                    Object o = createdByField.get(target);
                    return o.toString();
                } catch (IllegalAccessException e) {
                    log.error("Get Username is null", e);
                    return null;
                }
            }
        }
        return null;
    }
}
