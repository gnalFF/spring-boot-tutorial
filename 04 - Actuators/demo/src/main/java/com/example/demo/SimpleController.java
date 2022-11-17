package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.micrometer.core.annotation.Timed;

@Controller
public class SimpleController {
    
    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/")
    @Timed("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
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
