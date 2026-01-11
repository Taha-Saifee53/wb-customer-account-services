package com.wb.assignment.rabbitMQ;

import com.wb.assignment.model.enums.AccountType;
import com.wb.assignment.request.AccountCreateEvent;
import com.wb.assignment.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAccountConsumerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private CreateAccountConsumer createAccountConsumer;

    private AccountCreateEvent event;

    @BeforeEach
    void setUp() {
        event = AccountCreateEvent.builder()
                .customerId("1234567")
                .accountType(AccountType.CURRENT)
                .depositAmount(1000.00)
                .build();
    }

    @Test
    void consumeCustomerCreated_shouldCallAccountService() {
        // when
        createAccountConsumer.consumeCustomerCreated(event);

        // then
        verify(accountService, times(1)).createAccount(event);
        verifyNoMoreInteractions(accountService);
    }
}
