package com.yassirrijali.bankingbackend.dto;

import lombok.Data;
import java.util.Date;

@Data
public class CurrentBankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private String status;
    private CustomerDTO customer;
    private double overDraft;
}