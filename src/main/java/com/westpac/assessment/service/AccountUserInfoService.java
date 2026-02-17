package com.westpac.assessment.service;

import org.springframework.data.domain.PageRequest;
import java.util.List;

import org.springframework.stereotype.Service;

import com.westpac.assessment.exception.AccountNotFoundException;
import com.westpac.assessment.model.AccountUserInfo;
import com.westpac.assessment.repository.AccountUserInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountUserInfoService {
    private final AccountUserInfoRepository repository;

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
}
