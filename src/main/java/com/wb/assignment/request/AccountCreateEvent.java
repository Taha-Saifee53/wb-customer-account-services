package com.wb.assignment.request;

import com.wb.assignment.model.enums.AccountStatus;
import com.wb.assignment.model.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreateEvent implements Serializable {

    private String customerId;
    private String customerType;
    private AccountType accountType;
    private AccountStatus status;
    private String currency;
    private double depositAmount;
}

