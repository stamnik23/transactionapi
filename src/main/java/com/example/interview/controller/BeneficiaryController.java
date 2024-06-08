package com.example.interview.controller;

import com.example.interview.model.Account;
import com.example.interview.model.Beneficiary;
import com.example.interview.model.Transaction;
import com.example.interview.service.BeneficiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.interview.exception.ResourceNotFoundException;


import java.util.List;

@RestController
@RequestMapping("/beneficiaries")
public class BeneficiaryController {
    private static final Logger logger = LoggerFactory.getLogger(BeneficiaryController.class);


    @Autowired
    private BeneficiaryService beneficiaryService;



    @GetMapping("/{id}")
    public Beneficiary getBeneficiary(@PathVariable Long id) {
        return beneficiaryService.getBeneficiary(id);
    }

    @GetMapping("/{id}/accounts")
    public List<Account> getBeneficiaryAccounts(@PathVariable Long id) {
        return beneficiaryService.getBeneficiaryAccounts(id);
    }

    @GetMapping("/{id}/transactions")
    public List<Transaction> getBeneficiaryTransactions(@PathVariable Long id) {
        return beneficiaryService.getBeneficiaryTransactions(id);
    }

    @GetMapping("/{id}/balance")
    public Double getBeneficiaryBalance(@PathVariable Long id) {
        return beneficiaryService.getBeneficiaryBalance(id);
    }

    @GetMapping("/{id}/largest-withdrawal")
    public ResponseEntity<Transaction> getLargestWithdrawal(@PathVariable Long id) {
        try {
            Transaction largestWithdrawal = beneficiaryService.getLargestWithdrawal(id);
            return ResponseEntity.ok(largestWithdrawal);
        } catch (ResourceNotFoundException e) {
            logger.error("Resource not found: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}/largest-withdrawal-month")
    public ResponseEntity<Transaction> getLargestWithdrawalMonth(@PathVariable Long id) {
        try {
            Transaction largestWithdrawal = beneficiaryService.getLargestWithdrawalMonth(id);
            return ResponseEntity.ok(largestWithdrawal);
        } catch (ResourceNotFoundException e) {
            logger.error("Resource not found: {}", e.getMessage());
            throw e;
        }
    }
}
