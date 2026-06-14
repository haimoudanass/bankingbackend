package com.yassirrijali.bankingbackend.dto;

import lombok.Data;
import java.util.Date;

@Data
public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private double amount;
    private String type;
    private String description;
}