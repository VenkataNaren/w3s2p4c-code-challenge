package com.westpac.assessment.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AccountUserInfoRequest {
    @NotBlank
    @Email
    private String accountUserEmail;
    @NotBlank
    private String accountUserMobile;
    @NotBlank
    private String accountUserAddress;

    public String getAccountUserEmail() {
        return accountUserEmail;
    }

    public void setAccountUserEmail(String accountUserEmail) {
        this.accountUserEmail = accountUserEmail;
    }

    public String getAccountUserMobile() {
        return accountUserMobile;
    }

    public void setAccountUserMobile(String accountUserMobile) {
        this.accountUserMobile = accountUserMobile;
    }

    public String getAccountUserAddress() {
        return accountUserAddress;
    }

    public void setAccountUserAddress(String accountUserAddress) {
        this.accountUserAddress = accountUserAddress;
    }
}
