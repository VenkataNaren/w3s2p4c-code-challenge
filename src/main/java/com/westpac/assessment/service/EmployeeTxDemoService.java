package com.westpac.assessment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeTxDemoService {
    private final EmployeeService employeeService;

    @Transactional
    public String demoProxyInvocation() {
        String outer = currentTxName();
        String inner = employeeService.demoRequiresNew();
        return "outer=" + outer + ", inner=" + inner;
    }

    private String currentTxName() {
        return TransactionSynchronizationManager.getCurrentTransactionName();
    }
}
