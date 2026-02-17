package com.westpac.assessment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MinimumWithdrawalAmountException extends RuntimeException {
    public MinimumWithdrawalAmountException(String message) {
        super(message);
    }
}
