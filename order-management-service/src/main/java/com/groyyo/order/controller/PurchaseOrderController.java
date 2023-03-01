package com.groyyo.order.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PurchaseOrderController {
    @RequestMapping("/po")
    public String helloWorld() {
        return "Hello World from Spring Boot";
    }
}
