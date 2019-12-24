package com.esys.framework.uaa.captcha;

import com.esys.framework.core.configuration.EsysProperties;
import com.esys.framework.core.exceptions.ReCaptchaInvalidException;
import com.esys.framework.core.exceptions.ReCaptchaUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.regex.Pattern;

@Slf4j
@Service("captchaService")
public class CaptchaService implements ICaptchaService {
    private final static Logger LOGGER = LoggerFactory.getLogger(CaptchaService.class);

    // test için zorunluluğu kaldırıldı
    @Autowired(required = false)
    private HttpServletRequest request;

    @Autowired
    private EsysProperties esysProperties;

    @Autowired
    private ReCaptchaAttemptService reCaptchaAttemptService;

    @Autowired
    private RestOperations restTemplate;

    private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    @Override
    public void processResponse(final String response) {
        log.debug("Attempting to validate response {}", response);

        if (reCaptchaAttemptService.isBlocked(getClientIP())) {
            //Client exceeded maximum number of failed attempts
            throw new ReCaptchaInvalidException("invalid.captcha");
        }

        if (!responseSanityCheck(response)) {
            throw new ReCaptchaInvalidException("invalid.captcha");
        }

        final URI verifyUri = URI.create(String.format("https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s", getReCaptchaSecret(), response, getClientIP()));
        try {
            final GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);
            log.debug("Google's response: {} ", googleResponse.toString());

            if (!googleResponse.isSuccess()) {
                if (googleResponse.hasClientError()) {
                    reCaptchaAttemptService.reCaptchaFailed(getClientIP());
                }
                // reCaptcha was not successfully validated
                throw new ReCaptchaInvalidException("invalid.captcha");
            }
        } catch (RestClientException rce) {
            // Registration unavailable at this time.  Please try again later.
            throw new ReCaptchaUnavailableException("invalid.captcha", rce);
        }
        reCaptchaAttemptService.reCaptchaSucceeded(getClientIP());
    }

    private boolean responseSanityCheck(final String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }

    @Override
    public String getReCaptchaSite() {
        return esysProperties.getCaptcha().getGoogle().getKey().getSite();
    }

    @Override
    public String getReCaptchaSecret() {
        return esysProperties.getCaptcha().getGoogle().getKey().getSecret();
    }

    private String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
