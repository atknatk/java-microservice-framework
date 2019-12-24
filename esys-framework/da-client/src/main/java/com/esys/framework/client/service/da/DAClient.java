package com.esys.framework.client.service.da;

import com.esys.framework.client.contracts.da.DAContract;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * Mustafa Kerim Yılmaz
 * mustafa.yilmaz@isisbilisim.com.tr
 */
@FeignClient(value = "da-service")
public interface DAClient extends DAContract {
}
