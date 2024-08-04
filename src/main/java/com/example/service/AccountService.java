package com.example.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    } 

    public int userRegistration(Account account) {
        if ((account.getUsername().isBlank())||(account.getPassword().length() < 4)) {
            return -1;
        }
        else if (accountRepository.findAccountByUsername(account.getUsername()) != null) {
            return -2;
        }
        else {
            Account registered = accountRepository.save(account);
            return registered.getAccountId();
        }
    }

    public Account userLogin(String username, String password) {
        return accountRepository.findAccountByUsernameAndPassword(username, password);
    }
}
