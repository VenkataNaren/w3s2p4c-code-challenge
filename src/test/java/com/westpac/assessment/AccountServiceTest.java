package com.westpac.assessment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import com.westpac.assessment.exception.AccountNotFoundException;
import com.westpac.assessment.exception.InsufficientFundsException;
import com.westpac.assessment.exception.MinimumWithdrawalAmountException;
import com.westpac.assessment.model.Account;
import com.westpac.assessment.repository.AccountRepository;
import com.westpac.assessment.service.AccountService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceTest.class);

    @Mock
    private AccountRepository repository;

    @InjectMocks
    private AccountService service;

    @BeforeEach
    void logTestName(TestInfo testInfo) {
        logger.info("Running test: {}", testInfo.getDisplayName());
    }

    @Test
    void testWithdraw_InsufficientFunds_ThrowsException() {
        Account account = new Account();
        account.setBalance(new BigDecimal("100.00"));

        when(repository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(InsufficientFundsException.class, () -> {
            service.withdraw(1L, new BigDecimal("150.00"));
        });
    }

    @Test
    void testDeposit_Success_UpdatesBalance() {
        Account account = new Account();
        account.setBalance(new BigDecimal("100.00"));

        when(repository.findById(1L)).thenReturn(Optional.of(account));
        when(repository.save(account)).thenReturn(account);

        Account updated = service.deposit(1L, new BigDecimal("25.50"));

        assertEquals(new BigDecimal("125.50"), updated.getBalance());
        verify(repository).save(account);
    }

    @Test
    void testDeposit_Zero_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.deposit(1L, BigDecimal.ZERO);
        });
        verify(repository, never()).save(any(Account.class));
    }

    @Test
    void testDeposit_Negative_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.deposit(1L, new BigDecimal("-1.00"));
        });
        verify(repository, never()).save(any(Account.class));
    }

    @Test
    void testDeposit_AccountNotFound_ThrowsException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            service.deposit(1L, new BigDecimal("10.00"));
        });
    }

    @Test
    void testWithdraw_Success_UpdatesBalance() {
        Account account = new Account();
        account.setBalance(new BigDecimal("100.00"));

        when(repository.findById(1L)).thenReturn(Optional.of(account));
        when(repository.save(account)).thenReturn(account);

        Account updated = service.withdraw(1L, new BigDecimal("40.00"));

        assertEquals(new BigDecimal("60.00"), updated.getBalance());
        verify(repository).save(account);
    }

    @Test
    void testWithdraw_Zero_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.withdraw(1L, BigDecimal.ZERO);
        });
        verify(repository, never()).save(any(Account.class));
    }

    @Test
    void testWithdraw_Negative_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.withdraw(1L, new BigDecimal("-5.00"));
        });
        verify(repository, never()).save(any(Account.class));
    }

    @Test
    void testWithdraw_MinimumAmount_ThrowsException() {
        assertThrows(MinimumWithdrawalAmountException.class, () -> {
            service.withdraw(1L, new BigDecimal("10.00"));
        });
        verify(repository, never()).save(any(Account.class));
    }

    @Test
    void testWithdraw_AccountNotFound_ThrowsException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            service.withdraw(1L, new BigDecimal("20.00"));
        });
    }

    @Test
    void testCreateAccount_ValidInput_PersistsAccount() {
        when(repository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account created = service.createAccount("  Alice  ", new BigDecimal("50.00"));

        assertEquals("Alice", created.getAccountHolderName());
        assertEquals(new BigDecimal("50.00"), created.getBalance());
    }

    @Test
    void testCreateAccount_NullDeposit_DefaultsToZero() {
        when(repository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Account created = service.createAccount("Bob", null);

        assertEquals("Bob", created.getAccountHolderName());
        assertEquals(0, BigDecimal.ZERO.compareTo(created.getBalance()));
    }

    @Test
    void testCreateAccount_BlankName_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.createAccount("   ", new BigDecimal("10.00"));
        });
        verify(repository, never()).save(any(Account.class));
    }

    @Test
    void testCreateAccount_NegativeDeposit_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.createAccount("Charlie", new BigDecimal("-1.00"));
        });
        verify(repository, never()).save(any(Account.class));
    }
}
