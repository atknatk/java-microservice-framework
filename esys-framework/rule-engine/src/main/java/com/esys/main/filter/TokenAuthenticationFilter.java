package com.esys.main.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
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
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import com.esys.main.security.TokenHelper;
import com.esys.main.utils.JwtTokenDataHolder;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	TokenHelper tokenHelper;
	
	MessageSource messageSource;
	
	public TokenAuthenticationFilter(TokenHelper tokenHelper,MessageSource messageSource) {
		this.tokenHelper = tokenHelper;
		this.messageSource = messageSource;
	}

	public Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
		return (lastPasswordReset != null && created.before(lastPasswordReset));
	}
	
	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)throws IOException, ServletException {
		String authToken = null;
		try {	
			
			String authHeader = request.getHeader("Auth");
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				authToken = authHeader.substring(7);
			}
			if (authToken != null) {
				String uuid = request.getHeader("uuid");
				String traceId = response.getHeader("X-B3-TraceId");
				
				Map<String,Object> claims = tokenHelper.getAllClaimsFromToken(authToken);
				claims.put("uuid", uuid);
				claims.put("traceId", traceId);
				
				Map<String, Object> dataMap = JwtTokenDataHolder.getInstance().getData();
				if(dataMap != null) {
					dataMap.putAll(claims);
				}else {
					JwtTokenDataHolder.getInstance().setData(claims);
				}
				LocaleContextHolder.setLocale(request.getLocale());
				
				String username = JwtTokenDataHolder.getInstance().getUsername();
				if (username!=null && !username.equals("")) {
					
					//refresh Token				
					String newJwt = tokenHelper.refreshToken();
					
					HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(request);
				    requestWrapper.addHeader("Auth",newJwt);
				    response.setHeader("Auth", newJwt);
				    response.setHeader("Access-Control-Allow-Origin", "*");
				    response.setHeader("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT");
				    response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization,Auth");
				    
				    chain.doFilter(request, response);
					
					
					
									
				}else{
					log.error("UnAuthorizedException -- Status Code: {} {} Error Message: User is Null",HttpStatus.UNAUTHORIZED.value(),HttpStatus.UNAUTHORIZED.getReasonPhrase());
					response.setStatus(HttpStatus.UNAUTHORIZED.value());
					response.getWriter().write("User is Null");
				}			
							
			} else {
				log.error("UnAuthorizedException -- Status Code: {} {} Error Message: Token is Null",HttpStatus.UNAUTHORIZED.value(),HttpStatus.UNAUTHORIZED.getReasonPhrase());
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				response.getWriter().write("Token is Null");
			}
		} catch (IllegalStateException | IOException e ) {
			log.error("UnAuthorizedException -- Status Code: {} {} Error Message: Invalid Token authToken",HttpStatus.UNAUTHORIZED.value(),HttpStatus.UNAUTHORIZED.getReasonPhrase(),e);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter().write("Invalid Token authToken");
			String msg = messageSource.getMessage("error.UnAuthorizedException",null, request.getLocale());
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,msg);
		} catch (Exception e) {
			log.error("UnAuthorizedException -- Status Code: {} {} Error Message: Invalid Token authToken",HttpStatus.UNAUTHORIZED.value(),HttpStatus.UNAUTHORIZED.getReasonPhrase(),e);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter().write("Invalid Token authToken");
			String msg = messageSource.getMessage("error.UnAuthorizedException",null, request.getLocale());
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,msg);
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