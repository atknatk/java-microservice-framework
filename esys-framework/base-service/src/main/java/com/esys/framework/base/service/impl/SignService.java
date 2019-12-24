package com.esys.framework.base.service.impl;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import com.esys.framework.base.exceptions.ReportGenerationException;
import com.esys.framework.base.report.JasperReportsExporter;
import com.esys.framework.base.report.ReportColumn;
import com.esys.framework.base.service.IReportService;
import com.esys.framework.base.service.ISignService;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.coyote.http2.ByteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@Slf4j
public class SignService implements ISignService {

    @Override
    public byte[] sign(byte[] data, String keyFile) throws InvalidKeyException, Exception {
        Signature rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(getPrivate(keyFile));
        rsa.update(data);
        return rsa.sign();
    }

    public byte[] sign(String data, String keyFile) throws InvalidKeyException, Exception{
        return sign(data.getBytes(),keyFile);
    }

    public PrivateKey getPrivate(String filename) throws Exception {
        URL url = SignService.class.getClassLoader().getResource(filename);
        if(url == null){
            throw new IOException(filename);
        }
        byte[] keyBytes = ByteStreams.toByteArray(url.openStream());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }


    public boolean verifySignature(byte[] data, byte[] signature, String keyFile) throws Exception {
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(getPublic(keyFile));
        sig.update(data);
        return sig.verify(signature);
    }

    public PublicKey getPublic(String filename) throws Exception {
        URL url = SignService.class.getClassLoader().getResource(filename);
        if(url == null){
            throw new IOException(filename);
        }
        byte[] keyBytes = ByteStreams.toByteArray(url.openStream());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

}
