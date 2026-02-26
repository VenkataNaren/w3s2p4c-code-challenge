package com.westpac.assessment.exception;

public class BillingServiceUnAvailable extends RuntimeException {
    public BillingServiceUnAvailable(String message) {
        super(message);
    }
}
