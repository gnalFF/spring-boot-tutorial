package com.example.demo;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;

@Controller
public class SimpleController {

    @Value("${spring.application.name}")
    String appName;

    private final AtomicInteger gauge;

    public SimpleController(MeterRegistry meterRegistry){
        gauge =  meterRegistry.gauge("controller.homepage.gauge", Arrays.asList(), new AtomicInteger());
    }

    @GetMapping("/")
    @Timed("controllers.homepage")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        gauge.set(new Random().nextInt(10));
        return "home";
    }

    @GetMapping("/auth/home")
    public String anon(Model model) {
        model.addAttribute("appName", appName);
        return "auth";
    }

    @GetMapping("/admin/home")
    public String admin(Model model) {
        model.addAttribute("appName", appName);
        return "admin";
    }
}
