package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ErrorReportService {

    private static final Logger log = LoggerFactory.getLogger(ErrorReportService.class);
    private final RestTemplate restTemplate;

    private static final String REPORT_URL = "http://localhost:8080/mcp/demo"; // target endpoint
    private static final String BASE_MESSAGE = "naka-sho/demo1 repository error. Please explain in English the cause of the error and how to resolve it. \n trace : ";

    public ErrorReportService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    RestTemplate getRestTemplate() { // package-private for tests
        return restTemplate;
    }

    @Async
    public CompletableFuture<Void> reportError(Exception e) {
        String fullMessage = buildMessage(e);
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("query", fullMessage);
        try {
            restTemplate.postForEntity(REPORT_URL, form, String.class);
            log.info("Posted error report to {}", REPORT_URL);
        } catch (Exception postEx) {
            log.error("Failed to post error report", postEx);
        }
        return CompletableFuture.completedFuture(null);
    }

    private String buildMessage(Exception e) {
        return BASE_MESSAGE + "\n" + e. getMessage() + "\n" + Arrays
                .stream(e.getStackTrace())
                .map(stackTraceElement -> stackTraceElement.toString())
                .collect(Collectors.joining("\n"));
    }
}
