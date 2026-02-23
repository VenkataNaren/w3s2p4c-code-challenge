package com.westpac.assessment.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateAccountRequest(
        @NotBlank(message = "Name is required") String accountHolderName,
        @NotNull @Positive BigDecimal amount) implements CreateAccountVersionedRequest {
}