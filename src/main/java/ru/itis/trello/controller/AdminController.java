package ru.itis.trello.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String hello(Model model) {
        model.addAttribute("css", "admin");
        model.addAttribute("link", "/logout");
        model.addAttribute("linkTitle", "logout");
        return "admin";
    }
}
