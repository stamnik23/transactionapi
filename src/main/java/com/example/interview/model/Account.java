package com.example.interview.model;

import jakarta.persistence.*;

@Entity
public class Account {
    @Id
    private Long accountId;
    private Long beneficiaryId;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(Long beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }
}
