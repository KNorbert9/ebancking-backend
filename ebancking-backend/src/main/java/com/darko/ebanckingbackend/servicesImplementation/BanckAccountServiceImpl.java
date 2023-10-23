package com.darko.ebanckingbackend.servicesImplementation;

import com.darko.ebanckingbackend.dtos.CustomerDTO;
import com.darko.ebanckingbackend.entities.*;
import com.darko.ebanckingbackend.enums.AccountStatus;
import com.darko.ebanckingbackend.enums.OperationType;
import com.darko.ebanckingbackend.exceptions.AccounBalanceNotInsuffisantException;
import com.darko.ebanckingbackend.exceptions.BankAccountNotFoundException;
import com.darko.ebanckingbackend.exceptions.CustomerNotFoundException;
import com.darko.ebanckingbackend.mappers.MappImplementation;
import com.darko.ebanckingbackend.repositories.AccountOperationRepo;
import com.darko.ebanckingbackend.repositories.BankAccountRepo;
import com.darko.ebanckingbackend.repositories.CustomerRepo;
import com.darko.ebanckingbackend.services.BanckAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BanckAccountServiceImpl implements BanckAccountService {

    private AccountOperationRepo accountOperationRepo;

    private BankAccountRepo bankAccountRepo;

    private CustomerRepo customerRepo;

    private MappImplementation dtoMapper;


    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("Création de nouveau client");
        return customerRepo.save(customer);
    }

    @Override
    public BankAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long CustumerId) {
        Customer customer = customerRepo.findById(CustumerId).orElse(null);

        if (customer == null)
            throw new CustomerNotFoundException("Customer Not Found");

        CurrentAccount currentAccount = new CurrentAccount();

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setStatus(AccountStatus.CREATED);
        currentAccount.setOverDraft(overDraft);

        return bankAccountRepo.save(currentAccount);
    }

    @Override
    public BankAccount saveSavingBankAccount(double initialBalance, double interestRate, Long CustumerId) {
        Customer customer = customerRepo.findById(CustumerId).orElse(null);

        if (customer == null)
            throw new CustomerNotFoundException("Customer Not Found");

        SavingAccount savingAccount = new SavingAccount();

        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setStatus(AccountStatus.CREATED);
        savingAccount.setInterestRate(interestRate);

        return bankAccountRepo.save(savingAccount);
    }


    @Override
    public List<CustomerDTO> listCustomer() {
        List<Customer> customers = customerRepo.findAll();

        List<CustomerDTO> customerDTOS;
        customerDTOS = customers.stream().map(customer -> dtoMapper.FromCustomer(customer)).collect(Collectors.toList());
        /*
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for (Customer customer:customers){
            CustomerDTO customerDTO = dtoMapper.FromCustomer(customer);
            customerDTOS.add(customerDTO);
        }
        *
         */

        return customerDTOS;

    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepo.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
        return bankAccount;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, AccounBalanceNotInsuffisantException {
        BankAccount bankAccount = getBankAccount(accountId);

        if (bankAccount.getBalance()<amount){
            throw new AccounBalanceNotInsuffisantException("Account Balance inssufisant");
        }

        AccountOperation accountOperation = new AccountOperation();

        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        accountOperationRepo.save(accountOperation);
        bankAccountRepo.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccount(accountId);

        AccountOperation accountOperation = new AccountOperation();

        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setBankAccount(bankAccount);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        accountOperationRepo.save(accountOperation);
        bankAccountRepo.save(bankAccount);
    }

    @Override
    public void transfert(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, AccounBalanceNotInsuffisantException {
        debit(accountIdSource, amount, "Vous avez débité une somme de"+amount+" de votre compte ");
        credit(accountIdSource, amount, "Une somme de "+amount+" a été transféré à votre compte depuis ");
    }

    @Override
    public List<BankAccount> listBankAccount() {
        return bankAccountRepo.findAll();
    }
}
