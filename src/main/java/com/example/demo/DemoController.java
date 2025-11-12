package com.example.demo;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoController {

    private static final List<Integer> integers = List.of(1, 2, 3);
    private final ErrorReportService errorReportService; // added

    public DemoController(ErrorReportService errorReportService) { // modified constructor
        this.errorReportService = errorReportService;
    }

    @GetMapping("/")
    public String integers() {
        return integers.toString();
    }

    @GetMapping("/id")
    public int integersById(@RequestParam("id") int id) {
        try {
            return integers.get(id);
        } catch (Exception e) {
            errorReportService.reportError(e); // async call
            throw new RuntimeException(e);
        }
    }
}
