package com.t2404e.jobboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public String helloPage() {
        return "hello"; // sẽ tìm file hello.html trong /templates
    }
}
