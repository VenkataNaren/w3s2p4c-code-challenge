package com.westpac.assessment.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreateAccountRequest(
        @NotBlank(message = "Name is required") String accountHolderName,
        @NotNull @Positive BigDecimal amount) {
}