package org.sid.bank_account_service.web;

import org.sid.bank_account_service.entities.BankAccount;
import org.sid.bank_account_service.entities.Customer;
import org.sid.bank_account_service.dto.BankAccountRequestDTO;
import org.sid.bank_account_service.dto.BankAccountResponseDTO;
import org.sid.bank_account_service.repositories.BankAccountRepository;
import org.sid.bank_account_service.repositories.CustomerRepository;
import org.sid.bank_account_service.service.AccountService;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BankAccountGraphQLController {

    private BankAccountRepository bankAccountRepository;
    private CustomerRepository customerRepository;
    private AccountService accountService;

    public BankAccountGraphQLController(
            BankAccountRepository bankAccountRepository,
            CustomerRepository customerRepository,
            AccountService accountService) {

        this.bankAccountRepository = bankAccountRepository;
        this.customerRepository = customerRepository;
        this.accountService = accountService;
    }

    @QueryMapping
    public List<BankAccount> accountsList() {
        return bankAccountRepository.findAll();
    }

    @QueryMapping
    public BankAccount bankAccountById(@Argument String id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Account %s not found", id)
                ));
    }

    @QueryMapping
    public List<Customer> customers() {
        return customerRepository.findAll();
    }

    @MutationMapping
    public BankAccountResponseDTO addAccount(
            @Argument BankAccountRequestDTO bankAccount) {

        return accountService.addAccount(bankAccount);
    }

    @MutationMapping
    public BankAccount updateAccount(
            @Argument String id,
            @Argument BankAccountRequestDTO bankAccount) {

        BankAccount account = bankAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Account %s not found", id)
                ));

        account.setBalance(bankAccount.getBalance());
        account.setCurrency(bankAccount.getCurrency());
        account.setType(bankAccount.getType());

        return bankAccountRepository.save(account);
    }

    @MutationMapping
    public Boolean deleteAccount(@Argument String id) {
        bankAccountRepository.deleteById(id);
        return true;
    }
}