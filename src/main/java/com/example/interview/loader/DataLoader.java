package com.example.interview.loader;

import com.example.interview.model.Account;
import com.example.interview.model.Beneficiary;
import com.example.interview.model.Transaction;
import com.example.interview.repository.AccountRepository;
import com.example.interview.repository.BeneficiaryRepository;
import com.example.interview.repository.TransactionRepository;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

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

    private void loadBeneficiaries() {
        try (Reader reader = new InputStreamReader(new ClassPathResource("beneficiaries.csv").getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {
            List<String[]> records = csvReader.readAll();
            if (records.size() > 0) {
                records.remove(0); // Remove header for xls file
            }
            for (String[] record : records) {
                if (record.length < 3) {
                    continue;
                }
                Beneficiary beneficiary = new Beneficiary();
                beneficiary.setBeneficiaryId(Long.parseLong(record[0]));
                beneficiary.setFirstName(record[1]);
                beneficiary.setLastName(record[2]);
                beneficiaryRepository.save(beneficiary);
                logger.info("Saved beneficiary: {}", beneficiary);
            }
        } catch (Exception e) {
            logger.error("Error loading beneficiaries", e);
        }
    }

    private void loadAccounts() {
        try (Reader reader = new InputStreamReader(new ClassPathResource("accounts.csv").getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {
            List<String[]> records = csvReader.readAll();
            if (records.size() > 0) {
                records.remove(0); // Remove header for xls file
            }
            for (String[] record : records) {
                if (record.length < 2) {
                    continue;
                }
                Account account = new Account();
                account.setAccountId(Long.parseLong(record[0]));
                account.setBeneficiaryId(Long.parseLong(record[1]));
                accountRepository.save(account);
                logger.info("Saved account: {}", account);
            }
        } catch (Exception e) {
            logger.error("Error loading accounts", e);
        }
    }

    private void loadTransactions() {
        try (Reader reader = new InputStreamReader(new ClassPathResource("transactions.csv").getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {
            List<String[]> records = csvReader.readAll();
            if (records.size() > 0) {
                records.remove(0); // Remove header for xls file
            }
            for (String[] record : records) {
                if (record.length < 4) {
                    continue;
                }
                Transaction transaction = new Transaction();
                transaction.setTransactionId(Long.parseLong(record[0]));
                transaction.setAccountId(Long.parseLong(record[1]));
                transaction.setAmount(Double.parseDouble(record[2]));
                transaction.setType(record[3]);
                transactionRepository.save(transaction);
                logger.info("Saved transaction: {}", transaction);
            }
        } catch (Exception e) {
            logger.error("Error loading transactions", e);
        }
    }
}
