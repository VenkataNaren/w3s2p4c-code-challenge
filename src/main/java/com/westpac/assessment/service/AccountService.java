package com.westpac.assessment.service;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import com.westpac.assessment.exception.AccountNotFoundException;
import com.westpac.assessment.exception.InsufficientFundsException;
import com.westpac.assessment.exception.MinimumWithdrawalAmountException;
import com.westpac.assessment.model.Account;
import com.westpac.assessment.repository.AccountRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository repository;
    private static final BigDecimal MIN_WITHDRAWAL = new BigDecimal("10.00");
    private static final String DEFAULT_ACTIVE = "Y";

    private static void validateWithdrawalAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount.compareTo(MIN_WITHDRAWAL) <= 0) {
            throw new MinimumWithdrawalAmountException(
                    "Withdrawal amount must be greater than " + MIN_WITHDRAWAL.toPlainString());
        }
    }

    private static String normalizeIsActive(String isActive) {
        if (isActive == null || isActive.trim().isEmpty()) {
            return DEFAULT_ACTIVE;
        }
        String value = isActive.trim().toUpperCase();
        if (!value.equals("Y") && !value.equals("N")) {
            throw new IllegalArgumentException("isActive must be 'Y' or 'N'");
        }
        return value;
    }

    @Transactional
    public Account deposit(Long id, BigDecimal amount) {
        // BUG FIX: Prevent negative deposits
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        Account account = repository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account " + id + " not found"));

        account.setBalance(account.getBalance().add(amount));
        return repository.save(account);
    }

    @Transactional
    public Account withdraw(Long id, BigDecimal amount) {
        validateWithdrawalAmount(amount);

        Account account = repository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account " + id + " not found"));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds: Overdraft not permitted");
        }

        account.setBalance(account.getBalance().subtract(amount));
        return repository.save(account);
    }

    @Transactional // Always use transactional for persistence
    public Account createAccount(String name, BigDecimal initialDeposit) {
        return createAccount(name, initialDeposit, DEFAULT_ACTIVE);
    }

    @Transactional
    public Account createAccount(String name, BigDecimal initialDeposit, String isActive) {
        // 1. Validation: Business rules should be in the service, not just the DTO
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder name cannot be empty");
        }

        // 2. State Integrity: Ensure we start with a valid balance (never null)
        BigDecimal startingBalance = (initialDeposit != null) ? initialDeposit : BigDecimal.ZERO;

        if (startingBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial deposit cannot be negative");
        }
        String normalizedIsActive = normalizeIsActive(isActive);

        // 3. Entity Mapping: Don't trust the Entity object passed from outside
        Account account = new Account();
        account.setAccountHolderName(name.trim());
        account.setBalance(startingBalance);
        account.setIsActive(normalizedIsActive);

        // 4. Persistence
        return repository.save(account);
    }

    @Transactional
    public Account getAccountByAccountId(Long accountId) {
        return repository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + accountId + " not found"));
    }

    @Transactional
    public void createAccountOrchestration() {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        try {
            for (int i = 0; i < 10; i++) {
                Account accountToProcess = new Account();
                accountToProcess.setAccountHolderName("ACC_NAME_" + i);
                accountToProcess.setBalance(BigDecimal.valueOf(100000));
                accountToProcess.setIsActive(DEFAULT_ACTIVE);
                executor.submit(() -> processAccountOrchestration(accountToProcess));
            }
        } finally {
            executor.shutdown();
        }

    }

    private void processAccountOrchestration(Account account) {
        repository.save(account);
    }
}
