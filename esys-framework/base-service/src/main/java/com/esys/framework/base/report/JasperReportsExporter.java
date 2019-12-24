package com.esys.framework.base.report;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface JasperReportsExporter {

	/**
	 * Verilen JasperPrint objesini export eder response'a yazar
	 * @param jp  JasperPrint
	 * @param fileName export File Name
	 * @param response Http Response
	 * @throws JRException JRException
	 * @throws IOException IOException
	 */
	void export(JasperPrint jp, String fileName, HttpServletResponse response) throws JRException, IOException;

	byte[] getByte();

	ByteArrayOutputStream getStream();
}
