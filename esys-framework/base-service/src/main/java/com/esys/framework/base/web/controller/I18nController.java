package com.esys.framework.base.web.controller;

import com.esys.framework.core.service.impl.I18nService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Tuple;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */

@RestController
@RequestMapping(value = "/i18n", produces = MediaType.APPLICATION_JSON_VALUE)
public class I18nController {


    @Autowired
    private I18nService service;

    @RequestMapping(value = "/{lang}", method = RequestMethod.GET)
    public Map<String, String> findUiByLanguage(@Valid @PathVariable("lang") String lang) {
        return service.findUiByLanguage(lang);
    }

}
