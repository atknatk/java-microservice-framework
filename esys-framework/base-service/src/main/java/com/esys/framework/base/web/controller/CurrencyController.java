package com.esys.framework.base.web.controller;

import com.codahale.metrics.annotation.Timed;
import com.esys.framework.base.dto.ProductDto;
import com.esys.framework.base.dto.ReportVersionDto;
import com.esys.framework.base.entity.Currency;
import com.esys.framework.base.enums.ReportName;
import com.esys.framework.base.reader.UploadFileResponse;
import com.esys.framework.base.report.JasperReportsCsvExporter;
import com.esys.framework.base.report.JasperReportsPdfExporter;
import com.esys.framework.base.report.JasperReportsXlsExporter;
import com.esys.framework.base.report.ReportColumn;
import com.esys.framework.base.service.ICurrencyService;
import com.esys.framework.base.service.IProductService;
import com.esys.framework.base.service.IReportService;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.core.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */

@RestController
@RequestMapping(value = "/currency", produces = MediaType.APPLICATION_JSON_VALUE)
public class CurrencyController extends BaseController {


    private transient ICurrencyService currencyService;


    @Autowired
    public CurrencyController(ICurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/today")
    @Timed
    public ModelResult<List<Currency>> today() {
        return new ModelResult.ModelResultBuilder().setData(currencyService.today(), LocaleContextHolder.getLocale()).success();
    }
}
