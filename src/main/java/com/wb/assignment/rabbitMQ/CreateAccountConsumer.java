package com.wb.assignment.rabbitMQ;

import com.wb.assignment.common.Constant;
import com.wb.assignment.request.AccountCreateEvent;
import com.wb.assignment.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateAccountConsumer {

    private final AccountService accountService;

    @RabbitListener(
            queues = Constant.ACCOUNT_CREATE_QUEUE,
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void consumeCustomerCreated(AccountCreateEvent event) {
        log.info("Received CustomerCreatedEvent: {}", event);
        accountService.createAccount(event);
    }
}

