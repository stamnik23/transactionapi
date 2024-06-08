package com.example.interview.loader;

import com.example.interview.model.Account;
import com.example.interview.model.Beneficiary;
import com.example.interview.model.Transaction;
import com.example.interview.repository.AccountRepository;
import com.example.interview.repository.BeneficiaryRepository;
import com.example.interview.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void run(String... args) throws Exception {
        loadBeneficiaries();
        loadAccounts();
        loadTransactions();
    }

    private void loadBeneficiaries() throws Exception {
        var resource = new ClassPathResource("beneficiaries.csv");
        try (var reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            var beneficiaries = reader.lines()
                    .skip(1)
                    .map(line -> {
                        var fields = line.split(",");
                        if (fields.length >= 3) {
                            var beneficiary = new Beneficiary();
                            beneficiary.setBeneficiaryId(Long.parseLong(fields[0]));
                            beneficiary.setFirstName(fields[1]);
                            beneficiary.setLastName(fields[2]);
                            return beneficiary;
                        } else {
                            System.err.println("Invalid beneficiary line: " + line);
                            return null;
                        }
                    })
                    .filter(beneficiary -> beneficiary != null)
                    .collect(Collectors.toList());

            beneficiaryRepository.saveAll(beneficiaries);
        }
    }

    private void loadAccounts() throws Exception {
        var resource = new ClassPathResource("accounts.csv");
        try (var reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            var accounts = reader.lines()
                    .skip(1)
                    .map(line -> {
                        var fields = line.split(",");
                        if (fields.length >= 2) {
                            var account = new Account();
                            account.setAccountId(Long.parseLong(fields[0]));
                            account.setBeneficiaryId(Long.parseLong(fields[1]));
                            return account;
                        } else {

                            System.err.println("Invalid account line: " + line);
                            return null;
                        }
                    })
                    .filter(account -> account != null)
                    .collect(Collectors.toList());

            accountRepository.saveAll(accounts);
        }
    }

    private void loadTransactions() throws Exception {
        var resource = new ClassPathResource("transactions.csv");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        try (var reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            var transactions = reader.lines()
                    .skip(1)
                    .map(line -> {
                        var fields = line.split(",");
                        if (fields.length >= 5) {
                            var transaction = new Transaction();
                            transaction.setTransactionId(Long.parseLong(fields[0]));
                            transaction.setAccountId(Long.parseLong(fields[1]));
                            transaction.setAmount(Double.parseDouble(fields[2]));
                            transaction.setType(fields[3]);
                            transaction.setDate(LocalDate.parse(fields[4], formatter));
                            return transaction;
                        } else {
                            System.err.println("Invalid transaction line: " + line);
                            return null;
                        }
                    })
                    .filter(transaction -> transaction != null)
                    .collect(Collectors.toList());

            transactionRepository.saveAll(transactions);
        }
    }
}
