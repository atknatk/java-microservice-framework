package com.esys.main.conf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.GenericFilterBean;

import com.esys.main.utils.JwtTokenDataHolder;

import brave.Span;
import brave.Tracer;


@Configuration
@Order(Integer.MIN_VALUE + 6)
public class SleuthConfig extends GenericFilterBean {

	
	@Autowired
	private Tracer tracer;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        Span currentSpan = this.tracer.currentSpan();
        
        HttpServletRequest request = ((HttpServletRequest) servletRequest);
        HttpServletResponse response = ((HttpServletResponse) servletResponse);
        
        String traceId = request.getHeader("X-B3-TraceId");
        
        if(traceId == null) {
        	traceId = currentSpan.context().traceIdString();  	
        }
        
        response.addHeader("X-B3-TraceId", traceId);   
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("traceId",traceId);
 		JwtTokenDataHolder.getInstance().setData(dataMap);
        chain.doFilter(request, response);
    }

}