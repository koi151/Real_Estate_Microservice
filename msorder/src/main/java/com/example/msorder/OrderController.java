package com.example.msorder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @GetMapping("/")
    public String gerOrders () {
        return "Order testing";
    }

}
