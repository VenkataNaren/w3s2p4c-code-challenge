package com.westpac.assessment.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

public record CreateAccountRequestV2(
        @NotNull @Valid AccountHolder holder,
        @NotNull @Valid InitialDeposit initialDeposit,
        @NotNull @Pattern(regexp = "Y|N", message = "isActive must be 'Y' or 'N'") String isActive) implements CreateAccountVersionedRequest {

    @Override
    public String accountHolderName() {
        return holder.fullName();
    }

    @Override
    public BigDecimal amount() {
        return initialDeposit.amount();
    }

    public record AccountHolder(
            @NotBlank String firstName,
            @NotBlank String lastName) {
        public String fullName() {
            String first = firstName == null ? "" : firstName.trim();
            String last = lastName == null ? "" : lastName.trim();
            String fullName = (first + " " + last).trim();
            return fullName.isEmpty() ? null : fullName;
        }
    }

    public record InitialDeposit(
            @NotNull @Positive BigDecimal amount,
            String currency) {
    }
}
