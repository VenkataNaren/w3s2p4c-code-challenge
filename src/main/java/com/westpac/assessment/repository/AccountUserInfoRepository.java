package com.westpac.assessment.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.westpac.assessment.model.AccountUserInfo;

@Repository
public interface AccountUserInfoRepository extends JpaRepository<AccountUserInfo, Long> {
    @Query("""
            select aui
            from AccountUserInfo aui
            join fetch aui.account
            where aui.account.id = :accountId
            order by aui.createdAt desc
            """)
    List<AccountUserInfo> findLatestByAccountId(Long accountId, Pageable pageable);

    @Query("""
            select aui
            from AccountUserInfo aui
            join fetch aui.account
            where aui.account.id = :accountId
            order by aui.createdAt desc
            """)
    List<AccountUserInfo> findAllByAccountIdOrderByCreatedAtDesc(Long accountId);
}
