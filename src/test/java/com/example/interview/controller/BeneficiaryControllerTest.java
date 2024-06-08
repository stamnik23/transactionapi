package com.example.interview.controller;

import com.example.interview.service.BeneficiaryService;
import com.example.interview.model.Beneficiary;
import com.example.interview.model.Account;
import com.example.interview.model.Transaction;
import com.example.interview.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(BeneficiaryController.class)
public class BeneficiaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BeneficiaryService beneficiaryService;

    @Test
    public void testGetBeneficiary() throws Exception {
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setBeneficiaryId(1L);
        beneficiary.setFirstName("Nikos");
        beneficiary.setLastName("Stamou");

        when(beneficiaryService.getBeneficiary(anyLong())).thenReturn(beneficiary);

        mockMvc.perform(get("/beneficiaries/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"beneficiaryId\":1,\"firstName\":\"Nikos\",\"lastName\":\"Stamou\"}"));
    }

    @Test
    public void testGetBeneficiaryNotFound() throws Exception {
        when(beneficiaryService.getBeneficiary(anyLong())).thenThrow(new ResourceNotFoundException("Beneficiary not found", 1L));

        mockMvc.perform(get("/beneficiaries/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetBeneficiaryBalance() throws Exception {
        when(beneficiaryService.getBeneficiaryBalance(anyLong())).thenReturn(-30.3);

        mockMvc.perform(get("/beneficiaries/1/balance"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("-30.3"));
    }

    @Test
    public void testGetBeneficiaryAccounts() throws Exception {
        Account account = new Account();
        account.setAccountId(1L);
        account.setBeneficiaryId(1L);

        when(beneficiaryService.getBeneficiaryAccounts(anyLong())).thenReturn(Arrays.asList(account));

        mockMvc.perform(get("/beneficiaries/1/accounts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"accountId\":1,\"beneficiaryId\":1}]"));
    }

    @Test
    public void testGetBeneficiaryTransactions() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setAccountId(1L);
        transaction.setAmount(100.0);
        transaction.setType("deposit");
        transaction.setDate(LocalDate.now());

        when(beneficiaryService.getBeneficiaryTransactions(anyLong())).thenReturn(Arrays.asList(transaction));

        mockMvc.perform(get("/beneficiaries/1/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"transactionId\":1,\"accountId\":1,\"amount\":100.0,\"type\":\"deposit\",\"date\":\"" + LocalDate.now().toString() + "\"}]"));
    }

    @Test
    public void testGetLargestWithdrawal() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setAccountId(1L);
        transaction.setAmount(200.0);
        transaction.setType("withdrawal");
        transaction.setDate(LocalDate.now().minusDays(15));

        when(beneficiaryService.getLargestWithdrawal(anyLong())).thenReturn(transaction);

        mockMvc.perform(get("/beneficiaries/1/largest-withdrawal"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"transactionId\":1,\"accountId\":1,\"amount\":200.0,\"type\":\"withdrawal\",\"date\":\"" + LocalDate.now().minusDays(15).toString() + "\"}"));
    }
}
