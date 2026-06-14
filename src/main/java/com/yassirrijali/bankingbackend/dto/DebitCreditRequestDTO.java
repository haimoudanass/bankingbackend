package com.yassirrijali.bankingbackend.dto;

import lombok.Data;

@Data
public class DebitCreditRequestDTO {
    private String accountId;
    private double amount;
    private String description;
}