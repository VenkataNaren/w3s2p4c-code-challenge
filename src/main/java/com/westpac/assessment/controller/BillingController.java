package com.westpac.assessment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.westpac.assessment.service.BillingServiceClient;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
public class BillingController {
    private final BillingServiceClient billingServiceClient;

    @GetMapping("/call")
    public ResponseEntity<String> callBillingService() {
        return ResponseEntity.ok(billingServiceClient.callBillingService());
    }
}
