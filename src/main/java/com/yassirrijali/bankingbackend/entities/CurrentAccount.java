package com.yassirrijali.bankingbackend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("CA")
@Data @NoArgsConstructor
public class CurrentAccount extends BankAccount {
    private double overDraft;
}