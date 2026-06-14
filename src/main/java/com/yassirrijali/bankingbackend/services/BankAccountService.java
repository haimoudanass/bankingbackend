package com.yassirrijali.bankingbackend.services;

import com.yassirrijali.bankingbackend.dto.*;
import com.yassirrijali.bankingbackend.exceptions.BalanceNotSufficientException;
import com.yassirrijali.bankingbackend.exceptions.BankAccountNotFoundException;
import com.yassirrijali.bankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    List<CustomerDTO> searchCustomers(String keyword);
    void deleteCustomer(Long customerId) throws CustomerNotFoundException;

    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<BankAccountDTO> bankAccountList();

    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String accountSource, String accountDestination, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<AccountOperationDTO> accountHistory(String accountId);
}