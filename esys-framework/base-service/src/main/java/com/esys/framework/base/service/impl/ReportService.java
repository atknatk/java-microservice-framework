package com.esys.framework.base.service.impl;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DJWaterMark;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import com.esys.framework.base.consts.SignKey;
import com.esys.framework.base.entity.ReportVersion;
import com.esys.framework.base.enums.ReportName;
import com.esys.framework.base.exceptions.ReportGenerationException;
import com.esys.framework.base.report.JasperReportsExporter;
import com.esys.framework.base.report.JasperReportsHtmlExporter;
import com.esys.framework.base.report.JasperReportsPdfExporter;
import com.esys.framework.base.report.ReportColumn;
import com.esys.framework.base.repository.IReportVersionRepository;
import com.esys.framework.base.service.IReportService;
import com.esys.framework.base.service.ISignService;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

@Service
@Slf4j
public class ReportService implements IReportService {

    private static final Version REPORT_MAPPER_MODULE_VERSION = new Version(0, 0, 0, (String)null);
    private transient MessageSource messageSource;
    private transient ISignService signService;

    @Getter
    private transient ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
            .configure(DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS, false);
    private transient IReportVersionRepository reportVersionRepository;

    @Autowired
    public ReportService(MessageSource messageSource,ISignService signService,IReportVersionRepository reportVersionRepository) {
        this.messageSource = messageSource;
        this.signService = signService;
        this.reportVersionRepository = reportVersionRepository;
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        SimpleModule simpleModule = new SimpleModule("ReportMapperModule", REPORT_MAPPER_MODULE_VERSION);
   //     simpleModule.addDeserializer(ProductDto.class,ProductDtoDeserializer.INSTANCE);
        objectMapper.registerModule(simpleModule);
    }

    @Override
    public void export(List list, String[] datatablesColumns, Map<String, ReportColumn> columnMap, HttpServletResponse response, JasperReportsExporter exporter, String fileName, boolean isOldVersion) {
        export(list, datatablesColumns, columnMap,response, exporter, fileName,isOldVersion,null,null);
    }

    @Override
    public void export(List list, String[] datatablesColumns, Map<String, ReportColumn> columnMap, HttpServletResponse response, JasperReportsExporter exporter, String fileName, boolean isOldVersion, @Nullable ReportName reportName) {
        export(list, datatablesColumns, columnMap,response, exporter, fileName,isOldVersion,reportName,null);
    }


    public void export(List list, String[] datatablesColumns, Map<String, ReportColumn> columnMap,
                       HttpServletResponse response, JasperReportsExporter exporter, String fileName,
                       boolean isOldVersion, @Nullable ReportName reportName, @Nullable String watermark) {
        Locale locale;
        if(response == null){
            locale = LocaleContextHolder.getLocale();
        }else{
            locale = response.getLocale();
        }

        if (list == null || list.isEmpty()) {
            return;
        }

        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        Color veryLightGrey = new Color(230,230,230);
        oddRowStyle.setBackgroundColor(veryLightGrey);
        oddRowStyle.setTransparency(ar.com.fdvs.dj.domain.constants.Transparency.OPAQUE);

        FastReportBuilder builder = new FastReportBuilder();
        builder.setOddRowBackgroundStyle(oddRowStyle);
        if(!Strings.isNullOrEmpty(watermark)) builder.addWatermark(new DJWaterMark(watermark));
        // http://dynamicjasper.com/2010/10/06/how-to-use-custom-jrxml-templates/
        if(exporter instanceof JasperReportsPdfExporter){
            builder.setTemplateFile("templates/reports/export_default.jrxml");
        }else{
            builder.setTemplateFile("templates/reports/export_data.jrxml");
        }

        if (datatablesColumns != null) {
            for (String column : datatablesColumns) {

                addColumnToReportBuilder(column,columnMap, builder, locale, fileName);
            }
        }

        builder.setUseFullPageWidth(true);

        JRDataSource ds = new JRBeanCollectionDataSource(list);

        JasperPrint jp;
        try {
            DynamicReport report = builder.build();
            jp = DynamicJasperHelper.generateJasperPrint(report, new ClassicLayoutManager(), ds);
        }
        catch (JRException e) {
            String errorMessage = messageSource.getMessage("error.exportingErrorException",
                    new Object[] {StringUtils.substringAfterLast(fileName, ".").toUpperCase()},
                    String.format("Error while exporting data to StringUtils file", StringUtils.
                            substringAfterLast(fileName, ".").toUpperCase()), locale);
            throw new ReportGenerationException(errorMessage);
        }

        try {
          if(reportName != null && !isOldVersion){
                String json = objectMapper.writeValueAsString(list);
                final byte[] sign = signService.sign(json, SignKey.PrivateKey);
                Optional<ReportVersion> verison = reportVersionRepository.findFirstByReportNameOrderByVersionDesc(reportName);
                if(!verison.isPresent()){
                    ReportVersion reportVersion = new ReportVersion();
                    reportVersion.setData(json);
                    reportVersion.setReport(exporter.getByte());
                    reportVersion.setSign(sign);
                    reportVersion.setCurrent(true);
                    reportVersion.setVersion(1);
                    reportVersion.setReportName(reportName);
                    reportVersionRepository.save(reportVersion);
                }else{
                    ReportVersion reportVersion  = verison.get();
                    if(!Arrays.equals(reportVersion.getSign(), sign)){
                        ReportVersion reportVersion2 = new ReportVersion();
                        reportVersion2.setData(json);
                        reportVersion2.setReport(exporter.getByte());
                        reportVersion2.setSign(sign);
                        reportVersion2.setVersion(reportVersion.getVersion()+1);
                        reportVersion2.setReportName(reportName);
                        reportVersion2.setCurrent(true);
                        reportVersionRepository.save(reportVersion2);

                        reportVersion.setCurrent(false);
                        reportVersionRepository.save(reportVersion);
                    }
                }
            }
            if(response != null){
                exporter.export(jp, fileName, response);
            }

        }
        catch (JRException e) {
            String errorMessage = messageSource.getMessage("error.exportingErrorException",
                    new Object[] {StringUtils.substringAfterLast(fileName, ".").toUpperCase()},
                    String.format("Error while exporting data to StringUtils file", StringUtils.
                            substringAfterLast(fileName, ".").toUpperCase()), locale);
            throw new ReportGenerationException(errorMessage);
        }
        catch (IOException e) {
            String errorMessage = messageSource.getMessage("error.exportingErrorException",
                    new Object[] {StringUtils.substringAfterLast(fileName, ".").toUpperCase()},
                    String.format("Error while exporting data to StringUtils file", StringUtils.
                            substringAfterLast(fileName, ".").toUpperCase()), locale);
            throw new ReportGenerationException(errorMessage);
        } catch (Exception e) {
            throw new ReportGenerationException(e);
        }
    }

    @Override
    public String export(List list, String mail, String[] datatablesColumns, Map<String, ReportColumn> columnMap, String fileName, @Nullable ReportName reportName) {
        Locale locale= LocaleContextHolder.getLocale();

        if (list == null || list.isEmpty()) {
            return "";
        }

        Style oddRowStyle = new Style();
        oddRowStyle.setBorder(Border.NO_BORDER());
        Color veryLightGrey = new Color(230,230,230);
        oddRowStyle.setBackgroundColor(veryLightGrey);
        oddRowStyle.setTransparency(ar.com.fdvs.dj.domain.constants.Transparency.OPAQUE);

        FastReportBuilder builder = new FastReportBuilder();
        builder.setOddRowBackgroundStyle(oddRowStyle);
        builder.setTemplateFile("templates/reports/export_default.jrxml");
        if (datatablesColumns != null) {
            for (String column : datatablesColumns) {

                addColumnToReportBuilder(column,columnMap, builder, locale, fileName);
            }
        }

        builder.setUseFullPageWidth(true);

        JRDataSource ds = new JRBeanCollectionDataSource(list);

        JasperPrint jp;
        try {
            DynamicReport report = builder.build();
            jp = DynamicJasperHelper.generateJasperPrint(report, new ClassicLayoutManager(), ds);
            JasperReportsExporter exporter = new JasperReportsHtmlExporter();
                exporter.export(jp, fileName, null);
                return exporter.getStream().toString(StandardCharsets.UTF_8.name());

        }
        catch (JRException e) {
            String errorMessage = messageSource.getMessage("error.exportingErrorException",
                    new Object[] {StringUtils.substringAfterLast(fileName, ".").toUpperCase()},
                    String.format("Error while exporting data to StringUtils ", StringUtils.
                            substringAfterLast(fileName, ".").toUpperCase()), locale);
            throw new ReportGenerationException(errorMessage);
        }catch (IOException e) {
            String errorMessage = messageSource.getMessage("error.exportingErrorException",
                    new Object[] {StringUtils.substringAfterLast(fileName, ".").toUpperCase()},
                    String.format("Error while exporting data to StringUtils IO ", StringUtils.
                            substringAfterLast(fileName, ".").toUpperCase()), locale);
            throw new ReportGenerationException(errorMessage);
        }

    }


    private void addColumnToReportBuilder(String columnName,  Map<String, ReportColumn> columnMap,FastReportBuilder builder, Locale locale, String fileName) {
        try {
            if(columnMap.containsKey(columnName)){
                ReportColumn reportColumn = columnMap.get(columnName);
                String name = messageSource.getMessage(reportColumn.getMessageKey(), null, locale);
                if(Strings.isNullOrEmpty(name)) name = reportColumn.getDefaultName();
                if(Strings.isNullOrEmpty(reportColumn.getPattern())){
                    builder.addColumn(name,columnName,reportColumn.getClassName(),reportColumn.getWidth(),reportColumn.isFixedWidth());
                }else{
                    builder.addColumn(name,columnName,reportColumn.getClassName(),reportColumn.getWidth(),reportColumn.isFixedWidth(),reportColumn.getPattern());
                }
            }
        }
        catch (ColumnBuilderException e) {
            String errorMessage = messageSource.getMessage("error.exportingErrorException",
                    new Object[] {StringUtils.substringAfterLast(fileName, ".").toUpperCase()},
                    String.format("Error while exporting data to StringUtils file", StringUtils.
                            substringAfterLast(fileName, ".").toUpperCase()), locale);
            throw new ReportGenerationException(errorMessage);
        }
        catch (ClassNotFoundException e) {
            String errorMessage = messageSource.getMessage("error.exportingErrorException",
                    new Object[] {StringUtils.substringAfterLast(fileName, ".").toUpperCase()},
                    String.format("Error while exporting data to StringUtils file", StringUtils.
                            substringAfterLast(fileName, ".").toUpperCase()), locale);
            throw new ReportGenerationException(errorMessage);
        }
    }

}
