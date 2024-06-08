package com.example.interview.controller;

import com.example.interview.exception.ResourceNotFoundException;
import com.example.interview.model.Account;
import com.example.interview.model.Beneficiary;
import com.example.interview.model.Transaction;
import com.example.interview.repository.AccountRepository;
import com.example.interview.repository.BeneficiaryRepository;

import com.example.interview.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BeneficiaryControllerTest {

    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private BeneficiaryController beneficiaryController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBeneficiary() {
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setBeneficiaryId(1L);
        beneficiary.setFirstName("John");
        beneficiary.setLastName("Doe");

        when(beneficiaryRepository.findById(1L)).thenReturn(Optional.of(beneficiary));

        Beneficiary result = beneficiaryController.getBeneficiary(1L);
        assertEquals(1L, result.getBeneficiaryId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    public void testGetBeneficiaryNotFound() {
        when(beneficiaryRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> beneficiaryController.getBeneficiary(1L));
    }

    @Test
    public void testGetBeneficiaryAccounts() {
        Account account1 = new Account();
        account1.setAccountId(1L);
        account1.setBeneficiaryId(1L);

        Account account2 = new Account();
        account2.setAccountId(2L);
        account2.setBeneficiaryId(1L);

        when(accountRepository.findByBeneficiaryId(1L)).thenReturn(Arrays.asList(account1, account2));

        List<Account> accounts = beneficiaryController.getBeneficiaryAccounts(1L);
        assertEquals(2, accounts.size());
    }

    @Test
    public void testGetBeneficiaryTransactions() {
        Account account1 = new Account();
        account1.setAccountId(1L);
        account1.setBeneficiaryId(1L);

        Account account2 = new Account();
        account2.setAccountId(2L);
        account2.setBeneficiaryId(1L);

        Transaction transaction1 = new Transaction();
        transaction1.setTransactionId(1L);
        transaction1.setAccountId(1L);
        transaction1.setAmount(200.0);
        transaction1.setType("deposit");

        Transaction transaction2 = new Transaction();
        transaction2.setTransactionId(2L);
        transaction2.setAccountId(2L);
        transaction2.setAmount(-100.0);
        transaction2.setType("withdrawal");

        when(accountRepository.findByBeneficiaryId(1L)).thenReturn(Arrays.asList(account1, account2));
        when(transactionRepository.findByAccountIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(transaction1, transaction2));

        List<Transaction> transactions = beneficiaryController.getBeneficiaryTransactions(1L);
        assertEquals(2, transactions.size());
    }

    @Test
    public void testGetBeneficiaryBalance() {
        Account account1 = new Account();
        account1.setAccountId(1L);
        account1.setBeneficiaryId(1L);

        Account account2 = new Account();
        account2.setAccountId(2L);
        account2.setBeneficiaryId(1L);

        Transaction transaction1 = new Transaction();
        transaction1.setTransactionId(1L);
        transaction1.setAccountId(1L);
        transaction1.setAmount(200.0);
        transaction1.setType("deposit");

        Transaction transaction2 = new Transaction();
        transaction2.setTransactionId(2L);
        transaction2.setAccountId(2L);
        transaction2.setAmount(100.0);
        transaction2.setType("withdrawal");

        Transaction transaction3 = new Transaction();
        transaction3.setTransactionId(3L);
        transaction3.setAccountId(1L);
        transaction3.setAmount(200.0);
        transaction3.setType("deposit");

        when(accountRepository.findByBeneficiaryId(1L)).thenReturn(Arrays.asList(account1, account2));
        when(transactionRepository.findByAccountIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(transaction1, transaction2, transaction3));

        Double balance = beneficiaryController.getBeneficiaryBalance(1L);
        assertEquals(300.0, balance);
    }

    @Test
    public void testGetLargestWithdrawal() {
        Account account1 = new Account();
        account1.setAccountId(1L);
        account1.setBeneficiaryId(1L);

        Account account2 = new Account();
        account2.setAccountId(2L);
        account2.setBeneficiaryId(1L);

        Transaction transaction1 = new Transaction();
        transaction1.setTransactionId(1L);
        transaction1.setAccountId(1L);
        transaction1.setAmount(50.0);
        transaction1.setType("withdrawal");

        Transaction transaction2 = new Transaction();
        transaction2.setTransactionId(2L);
        transaction2.setAccountId(2L);
        transaction2.setAmount(20.0);
        transaction2.setType("withdrawal");

        when(accountRepository.findByBeneficiaryId(1L)).thenReturn(Arrays.asList(account1, account2));
        when(transactionRepository.findByAccountIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(transaction1, transaction2));

        Transaction largestWithdrawal = beneficiaryController.getLargestWithdrawal(1L);
        assertEquals(50.0, largestWithdrawal.getAmount());
    }

    @Test
    public void testGetLargestWithdrawalNotFound() {
        Account account1 = new Account();
        account1.setAccountId(1L);
        account1.setBeneficiaryId(1L);

        when(accountRepository.findByBeneficiaryId(1L)).thenReturn(Arrays.asList(account1));
        when(transactionRepository.findByAccountIdIn(Arrays.asList(1L))).thenReturn(Arrays.asList());

        assertThrows(ResourceNotFoundException.class, () -> beneficiaryController.getLargestWithdrawal(1L));
    }
}



