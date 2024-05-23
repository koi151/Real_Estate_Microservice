package com.koi151.msproperties.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("properties")
public class PropertiesController {

    @GetMapping("/")
    public String getProperties () {
        return "Get properties";
    }

}
