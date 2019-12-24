package com.esys.framework.base.service;

import com.esys.framework.base.enums.ReportName;
import com.esys.framework.base.report.JasperReportsExporter;
import com.esys.framework.base.report.ReportColumn;
import org.codehaus.jackson.map.ObjectMapper;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface IReportService {

    void export(List list, String[] datatablesColumns, Map<String, ReportColumn> columnMap, HttpServletResponse response, JasperReportsExporter exporter,
                String fileName, boolean isOldVersion);

    void export(List list, String[] datatablesColumns, Map<String, ReportColumn> columnMap, HttpServletResponse response, JasperReportsExporter exporter,
                String fileName, boolean isOldVersion, @Nullable ReportName reportName);

    void export(List list, String[] datatablesColumns, Map<String, ReportColumn> columnMap, HttpServletResponse response, JasperReportsExporter exporter,
                String fileName, boolean isOldVersion,@Nullable ReportName reportName,@Nullable String watermark);

    String export(List list, String mail, String[] datatablesColumns, Map<String, ReportColumn> columnMap, String fileName, @Nullable ReportName reportName);

    ObjectMapper getObjectMapper();
}
