package com.esys.framework.core.common;

import com.esys.framework.core.consts.ResultStatusCode;
import com.esys.framework.core.model.ModelResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;

@Slf4j
public class LongUtils {

    /**
     * Long değerinin null olup olmadığını kontrol eder
     * @param value alinan input degeri
     * @return eğer değer null ise true, null değil ise false döner
     */
    public static boolean isNullOrZero(Long value){
        if(value == null)
            return true;
        if(value.longValue() == 0){
            return true;
        }
        return false;
    }


    /**
     * Long değerinin null olup olmadığını kontrol eder
     * @param value value alinan input degeri
     * @param builders ModelResult builder degeri
     * @return eğer değer null ise ilgili mesajı ile birlikte geri döner.
     */
    public static ModelResult isNullOrZeroWithMessage(Long value, ModelResult.ModelResultBuilders builders){
        if(value == null){
            log.warn("id is null");
            builders.setMessageKey("core.dto.id.null");
            builders.setStatus(ResultStatusCode.RESOURCE_NOT_FOUND);
            return builders.build();
        }
        if(value.longValue() == 0){
            log.warn("id is zero");
            builders.setMessageKey("core.dto.id.zero");
            builders.setStatus(ResultStatusCode.RESOURCE_NOT_FOUND);
            return builders.build();
        }
        return builders.setStatus(ResultStatusCode.SUCCESS).build();
    }

    /**
     * Long değerinin null olup olmadığını kontrol eder
     * @param value value alinan input degeri
     * @param builders ModelResult builder degeri
     * @return eğer değer null ise ilgili mesajı ile birlikte geri döner.
     */
    public static ModelResult isNullOrZeroWithMessage(Long value, ModelResult.ModelResultBuilder builders){
        if(value == null){
            log.warn("id is null");
            builders.setMessageKey("core.dto.id.null");
            builders.setStatus(ResultStatusCode.RESOURCE_NOT_FOUND);
            return builders.build();
        }
        if(value.longValue() == 0){
            log.warn("id is zero");
            builders.setMessageKey("core.dto.id.zero");
            builders.setStatus(ResultStatusCode.RESOURCE_NOT_FOUND);
            return builders.build();
        }
        return builders.setStatus(ResultStatusCode.SUCCESS).build();
    }


}
