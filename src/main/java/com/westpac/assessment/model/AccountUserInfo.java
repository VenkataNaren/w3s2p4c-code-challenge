package com.westpac.assessment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Table(name = "account_user_info", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "account_id", "created_at" })
})
@Data
public class AccountUserInfo {
    @Id
    @SequenceGenerator(name = "account_user_info_id_seq", sequenceName = "account_user_info_id_sequence", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_user_info_id_seq")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    private String accountUserEmail;
    private String accountUserMobile;
    private String accountUserAddress;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
