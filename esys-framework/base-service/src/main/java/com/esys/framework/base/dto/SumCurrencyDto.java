package com.esys.framework.base.dto;

import com.esys.framework.base.currency.Moneys;
import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.security.field.SecureField;
import com.esys.framework.core.security.field.policy.AuthorityBasedFieldSecurityPolicy;
import lombok.Data;

import java.util.Objects;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@Data
public class SumCurrencyDto extends AbstractDto{


    @SecureField(policyClasses = AuthorityBasedFieldSecurityPolicy.class,authorities = {"authority.price.dollar"})
    private float dollarPrice;
    private float gbpPrice;
    private float euroPrice;
    private float riyaPrice;
    private float turkishPrice;

}
