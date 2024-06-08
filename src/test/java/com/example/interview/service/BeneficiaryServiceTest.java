package com.example.interview.service;

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
import com.example.interview.exception.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class BeneficiaryServiceTest {

    @InjectMocks
    private BeneficiaryService beneficiaryService;

    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetBeneficiary() {
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setBeneficiaryId(1L);
        beneficiary.setFirstName("Nikos");
        beneficiary.setLastName("Stamou");

        when(beneficiaryRepository.findById(anyLong())).thenReturn(Optional.of(beneficiary));

        Beneficiary foundBeneficiary = beneficiaryService.getBeneficiary(1L);

        assertEquals(beneficiary, foundBeneficiary);
    }

    @Test
    public void testGetBeneficiaryNotFound() {
        when(beneficiaryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            beneficiaryService.getBeneficiary(1L);
        });
    }

   @Test
   public void testGetBeneficiaryBalance() {
       Account account1 = new Account();
       account1.setAccountId(1L);
       account1.setBeneficiaryId(1L);

       Account account2 = new Account();
       account2.setAccountId(2L);
       account2.setBeneficiaryId(1L);

       Transaction deposit1 = new Transaction();
       deposit1.setTransactionId(1L);
       deposit1.setAccountId(1L);
       deposit1.setAmount(200.0);
       deposit1.setType("deposit");
       deposit1.setDate(LocalDate.now());

       Transaction withdrawal1 = new Transaction();
       withdrawal1.setTransactionId(2L);
       withdrawal1.setAccountId(1L);
       withdrawal1.setAmount(100.0);
       withdrawal1.setType("withdrawal");
       withdrawal1.setDate(LocalDate.now());

       Transaction deposit2 = new Transaction();
       deposit2.setTransactionId(3L);
       deposit2.setAccountId(2L);
       deposit2.setAmount(150.0);
       deposit2.setType("deposit");
       deposit2.setDate(LocalDate.now());

       Transaction withdrawal2 = new Transaction();
       withdrawal2.setTransactionId(4L);
       withdrawal2.setAccountId(2L);
       withdrawal2.setAmount(50.0);
       withdrawal2.setType("withdrawal");
       withdrawal2.setDate(LocalDate.now());

       when(accountRepository.findByBeneficiaryId(anyLong())).thenReturn(Arrays.asList(account1, account2));
       when(transactionRepository.findByAccountId(1L)).thenReturn(Arrays.asList(deposit1, withdrawal1));
       when(transactionRepository.findByAccountId(2L)).thenReturn(Arrays.asList(deposit2, withdrawal2));

       Double balance = beneficiaryService.getBeneficiaryBalance(1L);


       assertEquals(200.0, balance);
   }


}
