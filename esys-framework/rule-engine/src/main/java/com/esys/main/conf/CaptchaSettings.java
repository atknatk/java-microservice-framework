package com.esys.main.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaptchaSettings {
 
	@Value("${google.recaptcha.key.site}")
	private String site;
	
	@Value("${google.recaptcha.key.secret}")
	private String secret;
	
	@Value("${google.recaptcha.siteVerifyUrl}")
	private String siteVerifyUrl;
	
}