package com.yassirrijali.bankingbackend.web;

import com.yassirrijali.bankingbackend.dto.*;
import com.yassirrijali.bankingbackend.exceptions.BalanceNotSufficientException;
import com.yassirrijali.bankingbackend.exceptions.BankAccountNotFoundException;
import com.yassirrijali.bankingbackend.exceptions.CustomerNotFoundException;
import com.yassirrijali.bankingbackend.services.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class BankAccountRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/{accountId}")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public List<BankAccountDTO> listAccounts() {
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/{accountId}/operations")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId) {
        return bankAccountService.accountHistory(accountId);
    }

    @PostMapping("/current")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public CurrentBankAccountDTO saveCurrentAccount(
            @RequestParam double initialBalance,
            @RequestParam double overDraft,
            @RequestParam Long customerId) throws CustomerNotFoundException {
        return bankAccountService.saveCurrentBankAccount(initialBalance, overDraft, customerId);
    }

    @PostMapping("/saving")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public SavingBankAccountDTO saveSavingAccount(
            @RequestParam double initialBalance,
            @RequestParam double interestRate,
            @RequestParam Long customerId) throws CustomerNotFoundException {
        return bankAccountService.saveSavingBankAccount(initialBalance, interestRate, customerId);
    }

    @PostMapping("/debit")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void debit(@RequestBody DebitCreditRequestDTO request) throws BankAccountNotFoundException, BalanceNotSufficientException {
        bankAccountService.debit(request.getAccountId(), request.getAmount(), request.getDescription());
    }

    @PostMapping("/credit")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void credit(@RequestBody DebitCreditRequestDTO request) throws BankAccountNotFoundException {
        bankAccountService.credit(request.getAccountId(), request.getAmount(), request.getDescription());
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void transfer(@RequestBody TransferRequestDTO request) throws BankAccountNotFoundException, BalanceNotSufficientException {
        bankAccountService.transfer(request.getAccountSource(), request.getAccountDestination(), request.getAmount(), request.getDescription());
    }
}
