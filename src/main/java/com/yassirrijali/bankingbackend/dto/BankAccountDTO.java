package com.yassirrijali.bankingbackend.dto;

import lombok.Data;
import java.util.Date;

@Data
public class BankAccountDTO {
    private String type; // "CurrentAccount" ou "SavingAccount"
    private String id;
    private double balance;
    private Date createdAt;
    private String status;
    private CustomerDTO customer;
    private double overDraft;      // utilisé seulement pour CurrentAccount
    private double interestRate;   // utilisé seulement pour SavingAccount
}