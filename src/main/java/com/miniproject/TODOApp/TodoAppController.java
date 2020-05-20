package com.miniproject.TODOApp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class TodoAppController {
    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }


}