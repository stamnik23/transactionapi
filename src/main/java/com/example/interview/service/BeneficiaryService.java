package com.example.interview.service;

import com.example.interview.exception.ResourceNotFoundException;
import com.example.interview.model.Account;
import com.example.interview.model.Beneficiary;
import com.example.interview.model.Transaction;
import com.example.interview.repository.AccountRepository;
import com.example.interview.repository.BeneficiaryRepository;
import com.example.interview.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
public class BeneficiaryService {

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Beneficiary getBeneficiary(Long id) {
        return beneficiaryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Beneficiary not found", id));
    }

    public List<Account> getBeneficiaryAccounts(Long id) {
        return accountRepository.findByBeneficiaryId(id);
    }

    public List<Transaction> getBeneficiaryTransactions(Long id) {
        List<Account> accounts = accountRepository.findByBeneficiaryId(id);
        List<Long> accountIds = accounts.stream()
                .map(Account::getAccountId)
                .collect(Collectors.toList());
        return transactionRepository.findByAccountIdIn(accountIds);
    }

    public Double getBeneficiaryBalance(Long id) {
        List<Account> accounts = accountRepository.findByBeneficiaryId(id);
        double balance = accounts.stream()
                .flatMap(account -> transactionRepository.findByAccountId(account.getAccountId()).stream())
                .mapToDouble(transaction -> {
                    System.out.println("Processing transaction: " + transaction);
                    if ("deposit".equals(transaction.getType())) {
                        return transaction.getAmount();
                    } else if ("withdrawal".equals(transaction.getType())) {
                        return -transaction.getAmount();
                    } else {
                        return 0.0;
                    }
                }).sum();
        System.out.println("Calculated balance: " + balance);

        BigDecimal roundedBalance = new BigDecimal(balance).setScale(2, RoundingMode.HALF_UP);
        return roundedBalance.doubleValue();
    }

    public Transaction getLargestWithdrawalMonth(Long id) {
        List<Account> accounts = accountRepository.findByBeneficiaryId(id);
        if (accounts.isEmpty()) {
            throw new ResourceNotFoundException("No accounts found for beneficiary ID: " + id, id);
        }
        List<Long> accountIds = accounts.stream()
                .map(Account::getAccountId)
                .collect(Collectors.toList());
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);

        List<Transaction> withdrawals = transactionRepository.findByAccountIdIn(accountIds).stream()
                .filter(transaction -> "withdrawal".equals(transaction.getType()) && transaction.getDate().isAfter(oneMonthAgo))
                .collect(Collectors.toList());

        if (withdrawals.isEmpty()) {
            throw new ResourceNotFoundException("No transactions for that beneficiary ID in the last month", id);
        }

        return withdrawals.stream()
                .max(Comparator.comparingDouble(Transaction::getAmount))
                .orElseThrow(() -> new ResourceNotFoundException("No withdrawals found for beneficiary", id));
    }

    public Transaction getLargestWithdrawal(Long id) {
        List<Account> accounts = accountRepository.findByBeneficiaryId(id);
        if (accounts.isEmpty()) {
            throw new ResourceNotFoundException("No accounts found for beneficiary ID: " + id, id);
        }
        List<Long> accountIds = accounts.stream()
                .map(Account::getAccountId)
                .collect(Collectors.toList());


        List<Transaction> withdrawals = transactionRepository.findByAccountIdIn(accountIds).stream()
                .filter(transaction -> "withdrawal".equals(transaction.getType()))
                .collect(Collectors.toList());

        if (withdrawals.isEmpty()) {
            throw new ResourceNotFoundException("No transactions for that beneficiary ID", id);
        }

        return withdrawals.stream()
                .max(Comparator.comparingDouble(Transaction::getAmount))
                .orElseThrow(() -> new ResourceNotFoundException("No withdrawals found for beneficiary", id));
    }
}
