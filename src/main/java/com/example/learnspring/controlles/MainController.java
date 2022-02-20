package com.example.learnspring.controlles;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Main page");
        return "home";
    }

    @GetMapping("/sign-in")
    public String sign_in(Model model) {
        model.addAttribute("title", "Sign in");
        return "sign-in";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "Page about us");
        return "about";
    }
}