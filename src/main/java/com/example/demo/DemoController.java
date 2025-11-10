package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoController {
    @GetMapping("/hello")
    public String hello() {
        List<Integer> integers = List.of(1, 2, 3);
        return integers.get(4) + "";
    }
}
