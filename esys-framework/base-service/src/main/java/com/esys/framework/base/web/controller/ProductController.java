package com.esys.framework.base.web.controller;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import com.codahale.metrics.annotation.Timed;
import com.esys.framework.base.currency.Moneys;
import com.esys.framework.base.dto.ProductDto;
import com.esys.framework.base.dto.ReportVersionDto;
import com.esys.framework.base.dto.SumCurrencyDto;
import com.esys.framework.base.entity.Product;
import com.esys.framework.base.entity.ReportVersion;
import com.esys.framework.base.enums.ImportSectionEnum;
import com.esys.framework.base.enums.ReportName;
import com.esys.framework.base.exceptions.ReportGenerationException;
import com.esys.framework.base.mail.OnMailSendEvent;
import com.esys.framework.base.reader.ImportReader;
import com.esys.framework.base.reader.UploadFileResponse;
import com.esys.framework.base.reader.excel.ExcelReader;
import com.esys.framework.base.report.*;
import com.esys.framework.base.service.IProductService;
import com.esys.framework.base.service.IReportService;
import com.esys.framework.core.consts.ResultStatusCode;
import com.esys.framework.core.dto.base.PasswordCheckDto;
import com.esys.framework.core.dto.organization.MainGroupDto;
import com.esys.framework.core.model.ModelResult;
import com.esys.framework.core.security.SecurityUtils;
import com.esys.framework.core.web.controller.BaseController;
import com.google.common.base.Strings;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */

@RestController
@RequestMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController extends BaseController {


    private transient IProductService productService;
    private transient IReportService reportService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    public ProductController(IProductService productService, IReportService reportService) {
        this.productService = productService;
        this.reportService = reportService;
    }

    @PostMapping
    @Timed
    @ResponseBody
    public ModelResult<ProductDto> saveRole(@RequestBody final ProductDto productDto) {
        final ProductDto dto = productService.save(productDto);
        return new ModelResult.ModelResultBuilder().setData(dto, LocaleContextHolder.getLocale()).success();
    }


    @GetMapping
    @Timed
    public ModelResult<List<ProductDto>> getAll() {
        final List<ProductDto> list = productService.findAll();
        return new ModelResult.ModelResultBuilder<List<ProductDto>>().setData(list).success();
    }

    @PostMapping("/upload")
    public ResponseEntity<ModelResult<UploadFileResponse>> uploadFile(@RequestParam("file") MultipartFile file) {
        productService.uploadProduct(file);

        return getResponseWithData(
                new ModelResult.ModelResultBuilder<UploadFileResponse>()
                        .setData(new UploadFileResponse(file.getName(), "",file.getContentType(), file.getSize()))
                        .success(),
                HttpStatus.OK);

    }

    @GetMapping("/version")
    public ResponseEntity<ModelResult<?>> version() {
        final ModelResult<List<ReportVersionDto>> listModelResult = productService.version();
        return getResponseWithData(listModelResult,HttpStatus.OK);

    }

    @GetMapping("/sum")
    public ResponseEntity sum() {
        return ResponseEntity.ok(productService.sum());

    }

    @GetMapping("/send")
    public ResponseEntity<ModelResult> sendMail(@RequestParam("datatablesColumns") String[] datatablesColumns, @RequestParam("version") Optional<Integer> version, @RequestParam("mail") String mail) {
        List list;
        String content;
        if(version.isPresent()){
            list = productService.getAllViaRevision(version.get());
            content = reportService.export(list, mail, datatablesColumns, productService.getColumnMap(), "products_report.html", ReportName.PRODUCT);
        }else{
            list = productService.findAll();
            content = reportService.export(list, mail,datatablesColumns, productService.getColumnMap(),  "products_report.html", ReportName.PRODUCT);
        }
        if(!Strings.isNullOrEmpty(content)){
            eventPublisher.publishEvent(new OnMailSendEvent(SecurityUtils.getCurrentBasicUser(),mail,"Ürün Listesi",content));
            final ModelResult success = ModelResult.ModelResultBuilders.getNew(messageSource).success();
            return ResponseEntity.ok(success);
        }

        final ModelResult err = ModelResult.ModelResultBuilders.getNew(messageSource).setStatus(ResultStatusCode.UNKNOWN_ERROR).build();
        return ResponseEntity.ok(err);
    }


    @PutMapping("/revert/{versionNo}")
    @PreAuthorize("checkPassword(#dto.password)")
    public ResponseEntity<ModelResult> revert(@PathVariable("versionNo") Integer versionNo,
                                              @Valid @RequestBody PasswordCheckDto dto) {
        productService.revert(versionNo);
        return getResponseWithData(ModelResult.ModelResultBuilders.getNew().success(),HttpStatus.OK);

    }

    @Timed
    @GetMapping(name = "exportPdf", value = "/export/pdf")
    public ResponseEntity<?> exportPdf(@RequestParam("datatablesColumns") String[] datatablesColumns, @RequestParam("version") Optional<Integer> version, HttpServletResponse response) {
        List list;
        if(version.isPresent()){
            list = productService.getAllViaRevision(version.get());
            reportService.export(list, datatablesColumns, productService.getColumnMap(), response, new JasperReportsPdfExporter(), "products_report.pdf",true, ReportName.PRODUCT,"OLD VERSION");
        }else{
            list = productService.findAll();
            reportService.export(list, datatablesColumns, productService.getColumnMap(), response, new JasperReportsPdfExporter(), "products_report.pdf",false, ReportName.PRODUCT);
        }
        return ResponseEntity.ok().build();

    }

    @Timed
    @GetMapping(name = "exportCsv", value = "/export/csv")
    public ResponseEntity<?> exportCsv(@RequestParam("datatablesColumns") String[] datatablesColumns,@Nullable @RequestParam("version") Optional<Integer> version, HttpServletResponse response) {
        List list;
        if(version.isPresent()){
            list = productService.getAllViaRevision(version.get());
            reportService.export(list, datatablesColumns, productService.getColumnMap(), response, new JasperReportsCsvExporter(), "products_report.csv", true,ReportName.PRODUCT,"OLD VERSION");
        }else{
            list = productService.findAll();
            reportService.export(list, datatablesColumns, productService.getColumnMap(), response, new JasperReportsCsvExporter(), "products_report.csv", false,ReportName.PRODUCT);
        }

        return ResponseEntity.ok().build();

    }

    @Timed
    @GetMapping(name = "exportXls", value = "/export/xls")
    public ResponseEntity<?> exportXls(@RequestParam("datatablesColumns") String[] datatablesColumns,@RequestParam("version") Optional<Integer> version, HttpServletResponse response) {
        List list;
        if(version.isPresent()){
            list = productService.getAllViaRevision(version.get());
            reportService.export(list, datatablesColumns, productService.getColumnMap(), response, new JasperReportsXlsExporter(), "products_report.xls", true,ReportName.PRODUCT, "OLD VERSION");
        }else{
            list = productService.findAll();
            reportService.export(list, datatablesColumns, productService.getColumnMap(), response, new JasperReportsXlsExporter(), "products_report.xls", false,ReportName.PRODUCT);
        }

        return ResponseEntity.ok().build();
    }





}
