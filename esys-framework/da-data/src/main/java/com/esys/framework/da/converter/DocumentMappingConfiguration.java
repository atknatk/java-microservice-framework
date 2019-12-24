package com.esys.framework.da.converter;

import com.esys.framework.core.converter.TypeMapConfigurer;
import com.esys.framework.core.dto.organization.CompanyDto;
import com.esys.framework.core.entity.organization.Company;
import com.esys.framework.core.entity.organization.Group;
import com.esys.framework.da.dto.DocumentDto;
import com.esys.framework.da.entity.Document;
import org.modelmapper.Provider;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentMappingConfiguration extends TypeMapConfigurer<Document, DocumentDto> {

    @Override
    public void toDto(TypeMap<Document, DocumentDto> typeMap) {
        typeMap.addMapping(src -> src.getCompany().getId(), DocumentDto::setIdCompany);
    }

    @Override
    public void toEntity(TypeMap<DocumentDto, Document> typeMap) {
        Provider<Group> groupProvider = req -> new Group();
        typeMap.addMappings(mapper -> mapper.<Long>map(src -> src.getIdCompany(), (dest, v) -> dest.getCompany().setId(v)));
    }
}
