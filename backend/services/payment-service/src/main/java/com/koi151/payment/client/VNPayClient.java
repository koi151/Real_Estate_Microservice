package com.koi151.payment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "vnpayClient", url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html")
public interface VNPayClient {
    @GetMapping("")
    boolean pay(@RequestParam("propertyId") Long propertyId,
                @RequestParam("accountId") String accountId,
                @RequestParam("amount") double amount);
}

