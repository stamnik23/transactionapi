package com.example.interview.repository;

import com.example.interview.model.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
}