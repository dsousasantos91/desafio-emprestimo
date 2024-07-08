package com.dsousasantos91.emprestimo.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class RequestResponseLoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic, if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // Wrap request to cache the body
        CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(httpServletRequest);

        // Wrap response to capture response body
        CustomHttpServletResponseWrapper responseWrapper = new CustomHttpServletResponseWrapper(httpServletResponse);

        // Log request URL and body
        String requestURL = cachedBodyHttpServletRequest.getRequestURL().toString();
        String requestBody = cachedBodyHttpServletRequest.getCachedBody();
        log.info("Request URL: " + requestURL);
        log.info("Request Body: " + requestBody);

        // Continue with the filter chain
        chain.doFilter(cachedBodyHttpServletRequest, responseWrapper);

        // Log response body
        String responseBody = new String(responseWrapper.getContentAsByteArray(), responseWrapper.getCharacterEncoding());
        log.info("Response Status: " + responseWrapper.getStatus());
        log.info("Response Body: " + responseBody);

        // Write captured response body back to the original response
        httpServletResponse.getOutputStream().write(responseWrapper.getContentAsByteArray());
    }

    @Override
    public void destroy() {
        // Cleanup logic, if needed
    }
}





