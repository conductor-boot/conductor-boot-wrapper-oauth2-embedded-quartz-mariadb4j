package com.my.conductorbootwrapper.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping ("/openapi")
    public String swaggerUI() {
	return "redirect:/swagger-ui.html";
    }
}