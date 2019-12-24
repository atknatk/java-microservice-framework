package com.esys.framework.da.dto;

import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.dto.organization.CompanyDto;
import com.esys.framework.core.entity.organization.Company;
import com.esys.framework.da.entity.Document;
import com.esys.framework.da.entity.DocumentVersion;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * Mustafa Kerim Yılmaz
 * mustafa.yilmaz@isisbilisim.com.tr
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto extends AbstractDto {

    @NotNull
    private Long idCompany;

    private String description;

    private Collection<DocumentVersion> versions;

    private byte[] data;

}
