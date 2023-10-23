package com.darko.ebanckingbackend;

import com.darko.ebanckingbackend.dtos.CustomerDTO;
import com.darko.ebanckingbackend.entities.AccountOperation;
import com.darko.ebanckingbackend.entities.CurrentAccount;
import com.darko.ebanckingbackend.entities.Customer;
import com.darko.ebanckingbackend.entities.SavingAccount;
import com.darko.ebanckingbackend.enums.AccountStatus;
import com.darko.ebanckingbackend.enums.OperationType;
import com.darko.ebanckingbackend.exceptions.AccounBalanceNotInsuffisantException;
import com.darko.ebanckingbackend.exceptions.BankAccountNotFoundException;
import com.darko.ebanckingbackend.exceptions.CustomerNotFoundException;
import com.darko.ebanckingbackend.repositories.AccountOperationRepo;
import com.darko.ebanckingbackend.repositories.BankAccountRepo;
import com.darko.ebanckingbackend.repositories.CustomerRepo;
import com.darko.ebanckingbackend.services.BanckAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbanckingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbanckingBackendApplication.class, args);
	}


	//@Bean
	CommandLineRunner start(BanckAccountService banckAccountService){
		return args -> {
			Stream.of("Norbert", "Darko", "Juls").forEach(name-> {
				CustomerDTO customer = new CustomerDTO();
				customer.setName(name);
				customer.setEmail(name + "@gmail.com");
				banckAccountService.saveCustomer(customer);
			});


			banckAccountService.listCustomer().forEach(cust -> {
				try {
					banckAccountService.saveCurrentBankAccount(Math.random()*9000, 9000, cust.getId());
					banckAccountService.saveSavingBankAccount(Math.random()*9000, 3.5, cust.getId());

					banckAccountService.listBankAccount().forEach(account -> {
						for (int i = 0; i < 10; i++){
							try {
								banckAccountService.credit(account.getId(), 1000+Math.random()*12000, "Credit ");
								banckAccountService.debit(account.getId(), 1000+Math.random()*9000, "debit");
							} catch (BankAccountNotFoundException | AccounBalanceNotInsuffisantException e) {
								throw new RuntimeException(e);
							}
						}
					});
				} catch (CustomerNotFoundException e){
					e.printStackTrace();
				}
			});


		};


	}

	//@Bean
	CommandLineRunner start(CustomerRepo customerRepo,
							BankAccountRepo bankAccountRepo,
							AccountOperationRepo accountOperationRepo){
		return args -> {
			Stream.of("Norbert", "Darko", "Juls").forEach(name-> {
				Customer customer = new Customer();
				customer.setName(name);
				customer.setEmail(name + "@gmail.com");
				customerRepo.save(customer);
			});

			customerRepo.findAll().forEach(cust-> {
				CurrentAccount currentAccount = new CurrentAccount();
				SavingAccount savingAccount = new SavingAccount();

				currentAccount.setId(UUID.randomUUID().toString());
				currentAccount.setBalance(Math.random()*9000);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setCustomer(cust);
				currentAccount.setOverDraft(90000);
				bankAccountRepo.save(currentAccount);


				savingAccount.setBalance(Math.random()*9000);
				savingAccount.setId(UUID.randomUUID().toString());
				savingAccount.setCreatedAt(new Date());
				savingAccount.setStatus(AccountStatus.CREATED);
				savingAccount.setCustomer(cust);
				savingAccount.setInterestRate(3.2);
				bankAccountRepo.save(savingAccount);
			});

			bankAccountRepo.findAll().forEach(account->{
				for (int i = 0; i<10;i++){
				AccountOperation accountOperation = new AccountOperation();

				accountOperation.setCreatedAt(new Date());
				accountOperation.setAmount(Math.random()*12000);
				accountOperation.setType(Math.random()>0.5? OperationType.DEBIT: OperationType.CREDIT);
				accountOperation.setBankAccount(account);
				accountOperationRepo.save(accountOperation);
				}
			});
		};
	}

}
