package com.dekhokaun.mindarobackend.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class RequestResponseLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Wrap request and response to cache content
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpResponse);

        long startTime = System.currentTimeMillis();
        String requestId = generateRequestId();

        try {
            // Log request details
            logRequest(requestWrapper, requestId);
            
            // Continue with the filter chain
            chain.doFilter(requestWrapper, responseWrapper);
            
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            
            // Log response details
            logResponse(responseWrapper, requestId, duration);
            
            // Copy response body back to the original response
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request, String requestId) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("\n========== INCOMING REQUEST ==========\n");
        logMessage.append("Request ID: ").append(requestId).append("\n");
        logMessage.append("Timestamp: ").append(LocalDateTime.now().format(formatter)).append("\n");
        logMessage.append("Method: ").append(request.getMethod()).append("\n");
        logMessage.append("URI: ").append(request.getRequestURI()).append("\n");
        
        // Query parameters
        String queryString = request.getQueryString();
        if (queryString != null) {
            logMessage.append("Query String: ").append(queryString).append("\n");
        }

        // Client information
        logMessage.append("\n--- Client Information ---\n");
        logMessage.append("Remote Address: ").append(request.getRemoteAddr()).append("\n");
        logMessage.append("Remote Host: ").append(request.getRemoteHost()).append("\n");
        logMessage.append("Remote Port: ").append(request.getRemotePort()).append("\n");
        logMessage.append("User Agent: ").append(request.getHeader("User-Agent")).append("\n");
        
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null) {
            logMessage.append("X-Forwarded-For: ").append(forwardedFor).append("\n");
        }

        // Request headers
        logMessage.append("\n--- Request Headers ---\n");
        Map<String, String> headers = getHeaders(request);
        headers.forEach((key, value) -> {
            // Mask sensitive headers
            if (key.equalsIgnoreCase("Authorization") || key.equalsIgnoreCase("Cookie")) {
                logMessage.append(key).append(": [MASKED]\n");
            } else {
                logMessage.append(key).append(": ").append(value).append("\n");
            }
        });

        // Request body
        String requestBody = getRequestBody(request);
        if (requestBody != null && !requestBody.isEmpty()) {
            logMessage.append("\n--- Request Body ---\n");
            logMessage.append(requestBody).append("\n");
        }

        logMessage.append("======================================\n");
        logger.info(logMessage.toString());
    }

    private void logResponse(ContentCachingResponseWrapper response, String requestId, long duration) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("\n========== OUTGOING RESPONSE ==========\n");
        logMessage.append("Request ID: ").append(requestId).append("\n");
        logMessage.append("Timestamp: ").append(LocalDateTime.now().format(formatter)).append("\n");
        logMessage.append("Status Code: ").append(response.getStatus()).append("\n");
        logMessage.append("Duration: ").append(duration).append(" ms\n");

        // Response headers
        logMessage.append("\n--- Response Headers ---\n");
        response.getHeaderNames().forEach(headerName -> {
            logMessage.append(headerName).append(": ").append(response.getHeader(headerName)).append("\n");
        });

        // Response body
        String responseBody = getResponseBody(response);
        if (responseBody != null && !responseBody.isEmpty()) {
            logMessage.append("\n--- Response Body ---\n");
            logMessage.append(responseBody).append("\n");
        }

        logMessage.append("=======================================\n");
        logger.info(logMessage.toString());
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            try {
                return new String(content, request.getCharacterEncoding());
            } catch (UnsupportedEncodingException e) {
                logger.error("Error reading request body", e);
                return "[Error reading request body]";
            }
        }
        return null;
    }

    private String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            try {
                return new String(content, response.getCharacterEncoding());
            } catch (UnsupportedEncodingException e) {
                logger.error("Error reading response body", e);
                return "[Error reading response body]";
            }
        }
        return null;
    }

    private String generateRequestId() {
        return String.format("%d-%d", System.currentTimeMillis(), Thread.currentThread().getId());
    }
}
