package com.esys.main.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import com.esys.main.security.TokenHelper;
import com.esys.main.utils.JwtTokenDataHolder;

public class JwtIntegrationKeyFilter extends OncePerRequestFilter {

	private Logger log = LoggerFactory.getLogger(getClass());

	TokenHelper tokenHelper;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	public JwtIntegrationKeyFilter(TokenHelper tokenHelper) {
		this.tokenHelper = tokenHelper;
	}

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String integrationKey = tokenHelper.getToken(request);
		try {
			if (integrationKey != null) {
				String platform = tokenHelper.getPlatformFromIntegrationKey(integrationKey);

				if (platform != null) {
					
					Map<String,Object> claims = tokenHelper.getAllClaimsFromIntegration(integrationKey);

					Map<String, Object> dataMap = JwtTokenDataHolder.getInstance().getData();
					if(dataMap != null) {
						dataMap.putAll(claims);
					}else {
						JwtTokenDataHolder.getInstance().setData(claims);
					}	

					chain.doFilter(request, response);
						
				} else {
					log.error("UnAuthorizedException TraceId:" + JwtTokenDataHolder.getInstance().getTraceId() + " Status Code:" + HttpStatus.UNAUTHORIZED.value() 
					+ " " + HttpStatus.UNAUTHORIZED.getReasonPhrase() + " Error Message: Platform is Null");
					response.setStatus(HttpStatus.UNAUTHORIZED.value());
					response.getWriter().write("Platform is Null");
				}
			} else {
				log.error("UnAuthorizedException TraceId:" + JwtTokenDataHolder.getInstance().getTraceId() + " Status Code:" + HttpStatus.UNAUTHORIZED.value() 
				+ " " + HttpStatus.UNAUTHORIZED.getReasonPhrase() + " Error Message: Integration Key is Null");
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				response.getWriter().write("Integration Key is Null");
			}
		} catch (IllegalStateException | IOException e) {
			log.error("UnAuthorizedException TraceId:" + JwtTokenDataHolder.getInstance().getTraceId() + " Status Code:" + HttpStatus.UNAUTHORIZED.value() 
			+ " " + HttpStatus.UNAUTHORIZED.getReasonPhrase() + " Error Message: Invalid Integration Key");
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter().write("Invalid Integration Key");
		} catch (Exception e) {
			log.error("UnAuthorizedException TraceId:" + JwtTokenDataHolder.getInstance().getTraceId() + " Status Code:" + HttpStatus.UNAUTHORIZED.value() 
			+ " " + HttpStatus.UNAUTHORIZED.getReasonPhrase() + " Error Message: Invalid Integration Key");

			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter().write("Invalid Integration Key");
		}
	}

	public class HeaderMapRequestWrapper extends HttpServletRequestWrapper {
		/**
		 * construct a wrapper for this request
		 * 
		 * @param request
		 */
		public HeaderMapRequestWrapper(HttpServletRequest request) {
			super(request);
		}

		private Map<String, String> headerMap = new HashMap<String, String>();

		/**
		 * add a header with given name and value
		 * 
		 * @param name
		 * @param value
		 */
		public void addHeader(String name, String value) {
			headerMap.put(name, value);
		}

		@Override
		public String getHeader(String name) {
			String headerValue = super.getHeader(name);
			if (headerMap.containsKey(name)) {
				headerValue = headerMap.get(name);
			}
			return headerValue;
		}

		/**
		 * get the Header names
		 */
		@Override
		public Enumeration<String> getHeaderNames() {
			List<String> names = Collections.list(super.getHeaderNames());
			for (String name : headerMap.keySet()) {
				names.add(name);
			}
			return Collections.enumeration(names);
		}

		@Override
		public Enumeration<String> getHeaders(String name) {
			List<String> values = Collections.list(super.getHeaders(name));
			if (headerMap.containsKey(name)) {
				values.add(headerMap.get(name));
			}
			return Collections.enumeration(values);
		}

	}
}