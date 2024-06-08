package com.example.interview.controller;

import com.example.interview.model.Account;
import com.example.interview.model.Beneficiary;
import com.example.interview.model.Transaction;
import com.example.interview.service.BeneficiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/beneficiaries")
public class BeneficiaryController {

    private static final Logger logger = LoggerFactory.getLogger(BeneficiaryController.class);

    @Autowired
    private BeneficiaryService beneficiaryService;

    @GetMapping("/{id}")
    public Beneficiary getBeneficiary(@PathVariable Long id) {
        logger.info("Fetching beneficiary with ID: {}", id);
        return beneficiaryService.getBeneficiary(id);
    }

    @GetMapping("/{id}/accounts")
    public List<Account> getBeneficiaryAccounts(@PathVariable Long id) {
        logger.info("Fetching accounts for beneficiary with ID: {}", id);
        return beneficiaryService.getBeneficiaryAccounts(id);
    }

    @GetMapping("/{id}/transactions")
    public List<Transaction> getBeneficiaryTransactions(@PathVariable Long id) {
        logger.info("Fetching transactions for beneficiary with ID: {}", id);
        return beneficiaryService.getBeneficiaryTransactions(id);
    }

    @GetMapping("/{id}/balance")
    public Double getBeneficiaryBalance(@PathVariable Long id) {
        logger.info("Fetching balance for beneficiary with ID: {}", id);
        return beneficiaryService.getBeneficiaryBalance(id);
    }

    @GetMapping("/{id}/largest-withdrawal")
    public Transaction getLargestWithdrawal(@PathVariable Long id) {
        logger.info("Fetching largest withdrawal for beneficiary with ID: {}", id);
        return beneficiaryService.getLargestWithdrawal(id);
    }
}
