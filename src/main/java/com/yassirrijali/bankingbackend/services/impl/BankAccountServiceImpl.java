package com.yassirrijali.bankingbackend.services.impl;

import com.yassirrijali.bankingbackend.dto.*;
import com.yassirrijali.bankingbackend.entities.*;
import com.yassirrijali.bankingbackend.exceptions.BalanceNotSufficientException;
import com.yassirrijali.bankingbackend.exceptions.BankAccountNotFoundException;
import com.yassirrijali.bankingbackend.exceptions.CustomerNotFoundException;
import com.yassirrijali.bankingbackend.repositories.AccountOperationRepository;
import com.yassirrijali.bankingbackend.repositories.BankAccountRepository;
import com.yassirrijali.bankingbackend.repositories.CustomerRepository;
import com.yassirrijali.bankingbackend.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;

    // ---- Customers ----
    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        Customer savedCustomer = customerRepository.save(customer);
        return convertToCustomerDTO(savedCustomer);
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return convertToCustomerDTO(customer);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return listCustomers();
        }
        return customerRepository.searchCustomer("%" + keyword + "%").stream()
                .map(this::convertToCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        customerRepository.delete(customer);
    }

    // ---- Bank Accounts ----
    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setStatus(AccountStatus.CREATED);
        currentAccount.setCustomer(customer);
        CurrentAccount saved = bankAccountRepository.save(currentAccount);
        return convertToCurrentDTO(saved);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setStatus(AccountStatus.CREATED);
        savingAccount.setCustomer(customer);
        SavingAccount saved = bankAccountRepository.save(savingAccount);
        return convertToSavingDTO(saved);
    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        return bankAccountRepository.findAll().stream()
                .map(this::convertToBankAccountDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Account not found"));
        return convertToBankAccountDTO(bankAccount);
    }

    // ---- Operations ----
    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Account not found"));
        if (account instanceof CurrentAccount) {
            double overDraft = ((CurrentAccount) account).getOverDraft();
            if (account.getBalance() + overDraft < amount)
                throw new BalanceNotSufficientException("Balance not sufficient");
        } else {
            if (account.getBalance() < amount)
                throw new BalanceNotSufficientException("Balance not sufficient");
        }
        AccountOperation operation = new AccountOperation();
        operation.setOperationDate(new Date());
        operation.setAmount(amount);
        operation.setType(OperationType.DEBIT);
        operation.setBankAccount(account);
        operation.setDescription(description);
        accountOperationRepository.save(operation);
        account.setBalance(account.getBalance() - amount);
        bankAccountRepository.save(account);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Account not found"));
        AccountOperation operation = new AccountOperation();
        operation.setOperationDate(new Date());
        operation.setAmount(amount);
        operation.setType(OperationType.CREDIT);
        operation.setBankAccount(account);
        operation.setDescription(description);
        accountOperationRepository.save(operation);
        account.setBalance(account.getBalance() + amount);
        bankAccountRepository.save(account);
    }

    @Override
    public void transfer(String accountSource, String accountDestination, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountSource, amount, "Transfer to " + accountDestination);
        credit(accountDestination, amount, "Transfer from " + accountSource);
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId) {
        return accountOperationRepository.findByBankAccountId(accountId).stream()
                .map(op -> {
                    AccountOperationDTO dto = new AccountOperationDTO();
                    dto.setId(op.getId());
                    dto.setOperationDate(op.getOperationDate());
                    dto.setAmount(op.getAmount());
                    dto.setType(op.getType().name());
                    dto.setDescription(op.getDescription());
                    return dto;
                }).collect(Collectors.toList());
    }

    // --- Méthodes de conversion ---
    private CustomerDTO convertToCustomerDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        return dto;
    }

    private CurrentBankAccountDTO convertToCurrentDTO(CurrentAccount account) {
        CurrentBankAccountDTO dto = new CurrentBankAccountDTO();
        dto.setId(account.getId());
        dto.setBalance(account.getBalance());
        dto.setCreatedAt(account.getCreatedAt());
        dto.setStatus(account.getStatus().name());
        dto.setCustomer(convertToCustomerDTO(account.getCustomer()));
        dto.setOverDraft(account.getOverDraft());
        return dto;
    }

    private SavingBankAccountDTO convertToSavingDTO(SavingAccount account) {
        SavingBankAccountDTO dto = new SavingBankAccountDTO();
        dto.setId(account.getId());
        dto.setBalance(account.getBalance());
        dto.setCreatedAt(account.getCreatedAt());
        dto.setStatus(account.getStatus().name());
        dto.setCustomer(convertToCustomerDTO(account.getCustomer()));
        dto.setInterestRate(account.getInterestRate());
        return dto;
    }

    private BankAccountDTO convertToBankAccountDTO(BankAccount account) {
        BankAccountDTO dto = new BankAccountDTO();
        dto.setId(account.getId());
        dto.setBalance(account.getBalance());
        dto.setCreatedAt(account.getCreatedAt());
        dto.setStatus(account.getStatus().name());
        dto.setCustomer(convertToCustomerDTO(account.getCustomer()));
        if (account instanceof CurrentAccount) {
            dto.setType("CurrentAccount");
            dto.setOverDraft(((CurrentAccount) account).getOverDraft());
        } else if (account instanceof SavingAccount) {
            dto.setType("SavingAccount");
            dto.setInterestRate(((SavingAccount) account).getInterestRate());
        }
        return dto;
    }
}