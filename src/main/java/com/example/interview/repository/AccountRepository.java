package com.example.interview.repository;

import com.example.interview.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByBeneficiaryId(Long beneficiaryId);
}