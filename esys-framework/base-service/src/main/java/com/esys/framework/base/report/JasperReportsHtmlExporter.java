package com.esys.framework.base.report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class JasperReportsHtmlExporter implements JasperReportsExporter {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();

	/**
	 * Verilen JasperPrint objesini HTML olarak export eder response'a yazar
	 * @param jp  JasperPrint
	 * @param fileName export File Name
	 * @param response Http Response
	 * @throws JRException JRException
	 * @throws IOException IOException
	 */

	@Override
	public void export(JasperPrint jp, String fileName, HttpServletResponse response) throws JRException, IOException {

		HtmlExporter exporter = new HtmlExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
		exporter.exportReport();
		if(response != null){
			response.setHeader("Content-Disposition", "inline; filename=" + fileName);
			response.setContentType("text/html");
			response.setContentLength(baos.size());
			ServletOutputStream outputStream = response.getOutputStream();
			baos.writeTo(outputStream);
			outputStream.flush();
		}

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
