package com.westpac.assessment.repository;

import com.westpac.assessment.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // Standard methods like findById, save, and delete are inherited automatically.
}