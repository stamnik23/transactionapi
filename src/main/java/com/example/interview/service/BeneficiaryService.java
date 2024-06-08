package com.example.interview.service;

import com.example.interview.model.Account;
import com.example.interview.model.Beneficiary;
import com.example.interview.model.Transaction;
import com.example.interview.repository.AccountRepository;
import com.example.interview.repository.BeneficiaryRepository;
import com.example.interview.repository.TransactionRepository;
import com.example.interview.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BeneficiaryService {

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Beneficiary getBeneficiary(Long id) {
        return beneficiaryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found"));
    }

    public List<Account> getBeneficiaryAccounts(Long id) {
        return accountRepository.findByBeneficiaryId(id);
    }

    public List<Transaction> getBeneficiaryTransactions(Long id) {
        List<Account> accounts = accountRepository.findByBeneficiaryId(id);
        List<Long> accountIds = accounts.stream().map(Account::getAccountId).collect(Collectors.toList());
        return transactionRepository.findByAccountIdIn(accountIds);
    }

    public Double getBeneficiaryBalance(Long id) {
        List<Account> accounts = accountRepository.findByBeneficiaryId(id);
        List<Long> accountIds = accounts.stream().map(Account::getAccountId).collect(Collectors.toList());
        List<Transaction> transactions = transactionRepository.findByAccountIdIn(accountIds);

        return transactions.stream()
                .mapToDouble(transaction -> "deposit".equals(transaction.getType()) ? transaction.getAmount() : -transaction.getAmount())
                .sum();
    }

    public Transaction getLargestWithdrawal(Long id) {
        List<Account> accounts = accountRepository.findByBeneficiaryId(id);
        if (accounts.isEmpty()) {
            throw new ResourceNotFoundException("No accounts found for beneficiary");
        }

        List<Long> accountIds = accounts.stream().map(Account::getAccountId).collect(Collectors.toList());
        List<Transaction> withdrawals = transactionRepository.findByAccountIdIn(accountIds).stream()
                .filter(transaction -> "withdrawal".equals(transaction.getType()))
                .collect(Collectors.toList());

        if (withdrawals.isEmpty()) {
            throw new ResourceNotFoundException("No withdrawals found for beneficiary");
        }

        return withdrawals.stream()
                .max(Comparator.comparing(Transaction::getAmount))
                .orElseThrow(() -> new ResourceNotFoundException("No largest withdrawal found"));
    }
}
