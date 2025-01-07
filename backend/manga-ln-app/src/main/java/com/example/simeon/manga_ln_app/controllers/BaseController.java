package com.example.simeon.manga_ln_app.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @GetMapping()
    public String hello(){
        return "hello this manga and ln app!";
    }
}
