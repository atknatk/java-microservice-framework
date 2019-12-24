package com.esys.framework.base.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;

public class JasperReportsCsvExporter implements JasperReportsExporter {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();

	/**
	 * Verilen JasperPrint objesini CSV olarak export eder response'a yazar
	 * @param jp  JasperPrint
	 * @param fileName export File Name
	 * @param response Http Response
	 * @throws JRException JRException
	 * @throws IOException IOException
	 */
	@Override
	public void export(JasperPrint jp, String fileName, HttpServletResponse response) throws JRException, IOException {

	 	JRCsvExporter exporter = new JRCsvExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		exporter.exportReport();
		response.setHeader("Content-Disposition", "inline; filename=" + fileName);
    	response.setContentType("text/csv");
		response.setContentLength(baos.size());
		ServletOutputStream outputStream = response.getOutputStream();
		baos.writeTo(outputStream);
		outputStream.flush();

	}


	@Override
	public byte[] getByte() {
		return baos.toByteArray();
	}

	@Override
	public ByteArrayOutputStream getStream() {
		return baos;
	}

}
