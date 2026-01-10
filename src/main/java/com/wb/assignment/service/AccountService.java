package com.wb.assignment.service;

import com.wb.assignment.model.entity.Account;
import com.wb.assignment.model.enums.AccountType;
import com.wb.assignment.repository.AccountRepository;
import com.wb.assignment.request.AccountCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * Called by RabbitMQ consumer when account need to be created
     */
    @Transactional
    public void createAccount(AccountCreateEvent event) {

        log.info("Creating account for customerId={}", event.getCustomerId());
        validateAccountRules(event.getCustomerId(), event.getAccountType());
        Account account = Account.builder()
                .accountId(generateAccountId(event.getCustomerId()))
                .customerId(event.getCustomerId())
                .accountType(event.getAccountType())
                .status(event.getStatus())
                .balance(BigDecimal.valueOf(event.getDepositAmount()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        accountRepository.save(account);

        log.info("Account created successfully for customerId={}", event.getCustomerId());
    }

    /**
     * Business rule validations
     */
    private void validateAccountRules(String customerId, AccountType accountType) {

        long accountCount = accountRepository.countByCustomerId(customerId);
        if (accountCount >= 10) {
            log.error(
                    "{} : {}", "ACCOUNT_LIMIT_EXCEEDED",
                    "Customer already has maximum allowed accounts");
        }

        if (accountRepository.existsByCustomerIdAndAccountType(customerId, accountType)) {
            log.error(
                    "{} : {}", "SALARY_ACCOUNT_EXISTS",
                    "Salary account already exists for customer");
        }
    }

    /**
     * Generates 10-digit account ID:
     * First 7 digits = customerId
     * Last 3 digits = sequence
     */
    private String generateAccountId(String customerId) {
        long existingAccounts = accountRepository.countByCustomerId(customerId) + 1;
        return String.format("%s%03d", customerId, existingAccounts);
    }
}

