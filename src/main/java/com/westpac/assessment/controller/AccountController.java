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

import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService service;
    private final AccountUserInfoService userInfoService;
    private final EmployeeService employeeService;

    @PostMapping("/{id}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable Long id, @Valid @RequestBody AmountRequest request) {
        return ResponseEntity.ok(service.deposit(id, request.amount()));
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<Account> withdraw(@PathVariable Long id, @Valid @RequestBody AmountRequest request) {
        return ResponseEntity.ok(service.withdraw(id, request.amount()));
    }

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        Account savedAccount = service.createAccount(
                request.accountHolderName(),
                request.amount());
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/user-info")
    public ResponseEntity<AccountUserInfo> addUserInfo(@PathVariable Long id,
            @Valid @RequestBody AccountUserInfoRequest request) {
        return new ResponseEntity<>(userInfoService.addUserInfo(
                id,
                request.getAccountUserEmail(),
                request.getAccountUserMobile(),
                request.getAccountUserAddress()), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/user-info")
    public ResponseEntity<AccountUserInfo> getLatestUserInfo(@PathVariable Long id) {
        return ResponseEntity.ok(userInfoService.getLatestByAccountId(id));
    }

    @GetMapping("/{id}/user-info/history")
    public ResponseEntity<List<AccountUserInfo>> getUserInfoHistory(@PathVariable Long id) {
        return ResponseEntity.ok(userInfoService.getHistoryByAccountId(id));
    }

    @GetMapping("/{id}/account-info")
    public ResponseEntity<Account> getAccountInfo(@PathVariable Long id) {
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
}
