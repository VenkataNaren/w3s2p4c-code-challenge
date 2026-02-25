package com.westpac.assessment.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.westpac.assessment.model.Account;
import com.westpac.assessment.model.AccountUserInfo;
import com.westpac.assessment.model.Employee;
import com.westpac.assessment.service.AccountService;
import com.westpac.assessment.service.AccountUserInfoService;
import com.westpac.assessment.service.EmployeeService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService service;
    private final AccountUserInfoService userInfoService;
    private final EmployeeService employeeService;
    private final ObjectMapper objectMapper;

    @PostMapping("/{id}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable("id") Long id, @Valid @RequestBody AmountRequest request) {
        return ResponseEntity.ok(service.deposit(id, request.amount()));
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<Account> withdraw(@PathVariable("id") Long id, @Valid @RequestBody AmountRequest request) {
        return ResponseEntity.ok(service.withdraw(id, request.amount()));
    }

    @PostMapping("/{customerId}/charge")
    public ResponseEntity<String> chargeCustomer(
            @PathVariable("customerId") String customerId) {
        return ResponseEntity.ok(service.chargeCustomer(customerId));
    }

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountVersionedRequest request) {
        Account savedAccount = service.createAccount(
                request.accountHolderName(),
                request.amount(),
                request.isActive());
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/user-info")
    public ResponseEntity<AccountUserInfo> addUserInfo(@PathVariable("id") Long id,
            @Valid @RequestBody AccountUserInfoRequest request) {
        return new ResponseEntity<>(userInfoService.addUserInfo(
                id,
                request.getAccountUserEmail(),
                request.getAccountUserMobile(),
                request.getAccountUserAddress()), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/user-info")
    public ResponseEntity<AccountUserInfo> getLatestUserInfo(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userInfoService.getLatestByAccountId(id));
    }

    @GetMapping("/{id}/user-info/history")
    public ResponseEntity<List<AccountUserInfo>> getUserInfoHistory(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userInfoService.getHistoryByAccountId(id));
    }

    @GetMapping("/{id}/account-info")
    public ResponseEntity<Account> getAccountInfo(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getAccountByAccountId(id));
    }

    @PostMapping("/add-employee")
    public ResponseEntity<Employee> addEmployeeToAccount(@Valid @RequestBody EmployeeRequest request) {
        return new ResponseEntity<>(employeeService.addEmployee(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.department()), HttpStatus.CREATED);
    }

    @GetMapping("/var/usage")
    public ResponseEntity<List<AccountUserInfo>> getVarUsage() {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9090/api/accounts/10000000013/user-info/history"))
                .build();

        try {
            var response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 400) {
                throw new IllegalStateException("Failed to fetch user info history. Status: " + response.statusCode());
            }
            var userInfos = objectMapper.readValue(response.body(), new TypeReference<List<AccountUserInfo>>() {
            });
            return ResponseEntity.ok(userInfos);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to fetch user info history", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Request interrupted", e);
        }
    }

    @PostMapping("/create/orchestration")
    public void createAccountOrchestration(@Valid @RequestBody CreateAccountRequest request) {
        service.createAccountOrchestration();
    }

}
