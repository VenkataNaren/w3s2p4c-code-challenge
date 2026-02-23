package com.westpac.assessment.model;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

@Entity
@Data
public class Account {
    @Id
    @SequenceGenerator(name = "account_id_seq", sequenceName = "account_id_sequence", // Ensure this name matches your
                                                                                      // SQL exactly
            initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_seq")
    private Long id;

    private String accountHolderName;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "is_active", length = 1)
    private String isActive = "Y";

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    private List<AccountUserInfo> userInfos;
}
