package com.wb.assignment.repository;

import com.wb.assignment.model.entity.Account;
import com.wb.assignment.model.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {

    long countByCustomerId(String customerId);

    boolean existsByCustomerIdAndAccountType(String customerId, AccountType accountType);
}

