package com.example.interview.controller;

import com.example.interview.model.Account;
import com.example.interview.model.Beneficiary;
import com.example.interview.model.Transaction;
import com.example.interview.repository.AccountRepository;
import com.example.interview.repository.BeneficiaryRepository;
import com.example.interview.repository.TransactionRepository;
import com.example.interview.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/beneficiaries")
public class BeneficiaryController {

    private static final Logger logger = LoggerFactory.getLogger(BeneficiaryController.class);

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/{id}")
    public Beneficiary getBeneficiary(@PathVariable Long id) {
        logger.info("Fetching beneficiary with ID: {}", id);
        return beneficiaryRepository.findById(id).orElseThrow(() -> {
            logger.error("Beneficiary not found with ID: {}", id);
            return new ResourceNotFoundException("Beneficiary not found");
        });
    }

    @GetMapping("/{id}/accounts")
    public List<Account> getBeneficiaryAccounts(@PathVariable Long id) {
        logger.info("Fetching accounts for beneficiary with ID: {}", id);
        List<Account> accounts = accountRepository.findByBeneficiaryId(id);
        logger.info("Accounts found: {}", accounts);
        return accounts;
    }

    @GetMapping("/{id}/transactions")
    public List<Transaction> getBeneficiaryTransactions(@PathVariable Long id) {
        logger.info("Fetching transactions for beneficiary with ID: {}", id);
        List<Account> accounts = accountRepository.findByBeneficiaryId(id);
        List<Long> accountIds = accounts.stream().map(Account::getAccountId).collect(Collectors.toList());
        logger.info("Account IDs for transactions: {}", accountIds);
        List<Transaction> transactions = transactionRepository.findByAccountIdIn(accountIds);
        logger.info("Transactions found: {}", transactions);
        return transactions;
    }

    @GetMapping("/{id}/balance")
    public Double getBeneficiaryBalance(@PathVariable Long id) {
        logger.info("Fetching balance for beneficiary with ID: {}", id);
        List<Account> accounts = accountRepository.findByBeneficiaryId(id);
        List<Long> accountIds = accounts.stream().map(Account::getAccountId).collect(Collectors.toList());
        List<Transaction> transactions = transactionRepository.findByAccountIdIn(accountIds);

        double balance = transactions.stream()
                .mapToDouble(transaction -> "deposit".equals(transaction.getType()) ? transaction.getAmount() : -transaction.getAmount())
                .sum();

        logger.info("Total balance for beneficiary with ID {}: {}", id, balance);
        return balance;
    }


    @GetMapping("/{id}/largest-withdrawal")
    public Transaction getLargestWithdrawal(@PathVariable Long id) {
        logger.info("Fetching largest withdrawal for beneficiary with ID: {}", id);
        List<Account> accounts = accountRepository.findByBeneficiaryId(id);
        if (accounts.isEmpty()) {
            logger.error("No accounts found for beneficiary with ID: {}", id);
            throw new ResourceNotFoundException("No accounts found for beneficiary");
        }

        List<Long> accountIds = accounts.stream().map(Account::getAccountId).collect(Collectors.toList());
        logger.info("Account IDs for beneficiary with ID {}: {}", id, accountIds);

        List<Transaction> withdrawals = transactionRepository.findByAccountIdIn(accountIds).stream()
                .filter(transaction -> "withdrawal".equals(transaction.getType()))
                .collect(Collectors.toList());

        logger.info("Withdrawals for beneficiary with ID {}: {}", id, withdrawals);

        if (withdrawals.isEmpty()) {
            logger.error("No withdrawals found for beneficiary with ID: {}", id);
            throw new ResourceNotFoundException("No withdrawals found for beneficiary");
        }

        Transaction largestWithdrawal = withdrawals.stream()
                .max(Comparator.comparing(Transaction::getAmount))
                .orElseThrow(() -> {
                    logger.error("No largest withdrawal found for beneficiary with ID: {}", id);
                    return new ResourceNotFoundException("No largest withdrawal found");
                });

        logger.info("Largest withdrawal for beneficiary with ID {}: {}", id, largestWithdrawal);
        return largestWithdrawal;
    }
}
