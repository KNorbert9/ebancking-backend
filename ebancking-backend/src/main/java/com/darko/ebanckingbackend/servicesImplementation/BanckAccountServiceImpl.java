package com.darko.ebanckingbackend.servicesImplementation;

import com.darko.ebanckingbackend.dtos.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Création de nouveau client");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer saveCustomer = customerRepo.save(customer);
        return dtoMapper.fromCustomer(saveCustomer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Mise à jour de client");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer saveCustomer = customerRepo.save(customer);
        return dtoMapper.fromCustomer(saveCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepo.deleteById(id);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long CustumerId) {
        Customer customer = customerRepo.findById(CustumerId).orElse(null);

        if (customer == null)
            throw new CustomerNotFoundException("Customer Not Found");

        CurrentAccount currentAccount = new CurrentAccount();

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setStatus(AccountStatus.CREATED);
        currentAccount.setOverDraft(overDraft);


        CurrentAccount currentAccountSave = bankAccountRepo.save(currentAccount);

        return dtoMapper.fromCurentBankAccount(currentAccountSave);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long CustumerId) {
        Customer customer = customerRepo.findById(CustumerId).orElse(null);

        if (customer == null)
            throw new CustomerNotFoundException("Customer Not Found");

        SavingAccount savingAccount = new SavingAccount();

        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setStatus(AccountStatus.CREATED);
        savingAccount.setInterestRate(interestRate);

        SavingAccount savingAccountSave = bankAccountRepo.save(savingAccount);


        return dtoMapper.fromSavingBankAccount(savingAccountSave);
    }


    @Override
    public List<CustomerDTO> listCustomer() {
        List<Customer> customers = customerRepo.findAll();

        List<CustomerDTO> customerDTOS;
        customerDTOS = customers.stream().map(customer -> dtoMapper.fromCustomer(customer)).collect(Collectors.toList());
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
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepo.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));

        if (bankAccount instanceof SavingAccount){
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return dtoMapper.fromSavingBankAccount(savingAccount);
        }else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return dtoMapper.fromCurentBankAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, AccounBalanceNotInsuffisantException {
        BankAccount bankAccount = bankAccountRepo.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));


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
        BankAccount bankAccount = bankAccountRepo.findById(accountId).orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));


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
    public List<BankAccountDTO> listBankAccount() {
        List<BankAccount> accountList = bankAccountRepo.findAll();

        List<BankAccountDTO> bankAccountDTOList = accountList.stream().map(bank -> {
            if (bank instanceof SavingAccount) {

                SavingAccount savingAccount = (SavingAccount) bank;
                return dtoMapper.fromSavingBankAccount(savingAccount);

            } else {

                CurrentAccount currentAccount = (CurrentAccount) bank;
                return dtoMapper.fromCurentBankAccount(currentAccount);

            }
        }).collect(Collectors.toList());

        return bankAccountDTOList;
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId) {
        List<AccountOperation> accountOperations = accountOperationRepo.findByBankAccountId(accountId);

        return accountOperations.stream().map(operation ->
                dtoMapper.fromAccountOperation(operation)
        ).toList();
    }

    @Override
    public CustomerDTO getCostomerDTOById(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepo.findById(id).orElseThrow(()->new CustomerNotFoundException("Customer non trouvé"));

        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepo.findById(accountId).orElse(null);
        if (bankAccount == null)
            throw new BankAccountNotFoundException("Compte non trouvé");
        Page<AccountOperation> accountOperations = accountOperationRepo.findByBankAccountId(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream().map(accountOperation ->
            dtoMapper.fromAccountOperation(accountOperation)).toList();
        accountHistoryDTO.setListOperations(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setTotalPage(accountOperations.getTotalPages());

        return accountHistoryDTO;
    }
}
