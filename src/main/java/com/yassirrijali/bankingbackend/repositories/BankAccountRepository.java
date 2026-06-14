package com.yassirrijali.bankingbackend.repositories;

import com.yassirrijali.bankingbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}