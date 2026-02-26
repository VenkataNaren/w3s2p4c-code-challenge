package com.westpac.assessment.service;

import org.springframework.stereotype.Service;

import com.westpac.assessment.exception.BillingServiceUnAvailable;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BillingServiceClient {

    @CircuitBreaker(name = "billingService", fallbackMethod = "fallbackBilling")
    public String callBillingService() {
        log.info("Calling billing service");
        double randomValue = Math.random();
        // Simulate slow service
        if (randomValue < 0.7) {
            log.warn("Billing service call failed - " + randomValue);
            throw new BillingServiceUnAvailable("Billing Service Down");
        }

        log.info("Billing service call succeeded");
        return "Billing Success - " + randomValue;
    }

    public String fallbackBilling(BillingServiceUnAvailable ex) {
        return "Billing temporarily unavailable. Please try later." + ex.getMessage();
    }
}
