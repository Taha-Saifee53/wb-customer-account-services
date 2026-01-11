package com.wb.assignment.service;

import com.wb.assignment.model.entity.Account;
import com.wb.assignment.model.enums.AccountStatus;
import com.wb.assignment.model.enums.AccountType;
import com.wb.assignment.repository.AccountRepository;
import com.wb.assignment.request.AccountCreateEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private AccountCreateEvent event;

    @BeforeEach
    void setUp() {
        event = AccountCreateEvent.builder()
                .customerId("1234567")
                .accountType(AccountType.SAVING)
                .status(AccountStatus.ACTIVE)
                .currency("USD")
                .depositAmount(5000.00)
                .build();
    }


    @Test
    void createAccount_successfulFlow() {

        when(accountRepository.countByCustomerId("1234567"))
                .thenReturn(2L);

        when(accountRepository.existsByCustomerIdAndAccountType(
                "1234567", AccountType.SALARY))
                .thenReturn(false);

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        accountService.createAccount(event);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());

        Account saved = captor.getValue();

        assertEquals("1234567003", saved.getAccountId());
        assertEquals("1234567", saved.getCustomerId());
        assertEquals(AccountType.SAVING, saved.getAccountType());
        assertEquals(AccountStatus.ACTIVE, saved.getStatus());
        assertEquals("USD", saved.getCurrency());
        assertEquals(BigDecimal.valueOf(5000.00), saved.getBalance());

        verify(accountRepository, times(2))
                .countByCustomerId("1234567");

        verify(accountRepository)
                .existsByCustomerIdAndAccountType("1234567", AccountType.SALARY);
    }


    @Test
    void createAccount_whenAccountLimitExceeded_stillSaves_dueToCurrentLogic() {

        when(accountRepository.countByCustomerId("1234567"))
                .thenReturn(10L);

        when(accountRepository.existsByCustomerIdAndAccountType(
                "1234567", AccountType.SALARY))
                .thenReturn(false);

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(() -> accountService.createAccount(event));

        verify(accountRepository).save(any(Account.class));
    }



    @Test
    void createAccount_whenSalaryAccountExists_andRequestIsSalary() {

        event.setAccountType(AccountType.SALARY);

        when(accountRepository.countByCustomerId("1234567"))
                .thenReturn(1L);

        when(accountRepository.existsByCustomerIdAndAccountType(
                "1234567", AccountType.SALARY))
                .thenReturn(true);

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        accountService.createAccount(event);

        verify(accountRepository).save(any(Account.class));
    }


    
    @Test
    void createAccount_generatesFirstAccountSequenceCorrectly() {

        when(accountRepository.countByCustomerId("1234567"))
                .thenReturn(0L);

        when(accountRepository.existsByCustomerIdAndAccountType(
                "1234567", AccountType.SALARY))
                .thenReturn(false);

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        accountService.createAccount(event);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());

        assertEquals("1234567001", captor.getValue().getAccountId());
    }
}
