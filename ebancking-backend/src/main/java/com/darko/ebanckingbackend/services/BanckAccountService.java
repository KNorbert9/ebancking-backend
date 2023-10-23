package com.darko.ebanckingbackend.services;

import com.darko.ebanckingbackend.dtos.CustomerDTO;
import com.darko.ebanckingbackend.entities.BankAccount;
import com.darko.ebanckingbackend.entities.Customer;
import com.darko.ebanckingbackend.exceptions.AccounBalanceNotInsuffisantException;
import com.darko.ebanckingbackend.exceptions.BankAccountNotFoundException;

import java.util.List;
import java.util.function.LongConsumer;

public interface BanckAccountService {

    Customer saveCustomer(Customer customer);

    BankAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long ConstumerId);

    BankAccount saveSavingBankAccount(double initialBalance, double interestRate, Long ConstumerId);

    List<CustomerDTO> listCustomer();

    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;

    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, AccounBalanceNotInsuffisantException;

    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;

    void transfert(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, AccounBalanceNotInsuffisantException;

    List<BankAccount> listBankAccount() ;
}
