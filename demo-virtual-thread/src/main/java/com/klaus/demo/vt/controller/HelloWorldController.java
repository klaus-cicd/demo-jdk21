package com.klaus.demo.vt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Klaus
 */
@RestController
public class HelloWorldController {

    @GetMapping
    public String helloWorld() {
        return "Hello World!";
    }
}
