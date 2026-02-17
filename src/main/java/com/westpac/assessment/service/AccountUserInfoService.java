package com.westpac.assessment.service;

import org.springframework.data.domain.PageRequest;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.westpac.assessment.exception.AccountNotFoundException;
import com.westpac.assessment.model.Account;
import com.westpac.assessment.model.AccountUserInfo;
import com.westpac.assessment.repository.AccountRepository;
import com.westpac.assessment.repository.AccountUserInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountUserInfoService {
    private final AccountUserInfoRepository repository;
    private final AccountRepository accountRepository;

    public AccountUserInfo getLatestByAccountId(Long accountId) {
        return repository.findLatestByAccountId(accountId, PageRequest.of(0, 1)).stream()
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account user info for account " + accountId + " not found"));
    }

    public List<AccountUserInfo> getHistoryByAccountId(Long accountId) {
        List<AccountUserInfo> results = repository.findAllByAccountIdOrderByCreatedAtDesc(accountId);
        if (results.isEmpty()) {
            throw new AccountNotFoundException("Account user info for account " + accountId + " not found");
        }
        return results;
    }

    public AccountUserInfo addUserInfo(Long accountId, String email, String mobile, String address) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account " + accountId + " not found"));

        AccountUserInfo userInfo = new AccountUserInfo();
        userInfo.setAccount(account);
        userInfo.setAccountUserEmail(email);
        userInfo.setAccountUserMobile(mobile);
        userInfo.setAccountUserAddress(address);
        userInfo.setCreatedAt(LocalDateTime.now());
        return repository.save(userInfo);
    }
}
