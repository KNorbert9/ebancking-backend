package com.darko.ebanckingbackend;

import com.darko.ebanckingbackend.entities.CurrentAccount;
import com.darko.ebanckingbackend.entities.Customer;
import com.darko.ebanckingbackend.entities.SavingAccount;
import com.darko.ebanckingbackend.enums.AccountStatus;
import com.darko.ebanckingbackend.repositories.AccountOperationRepo;
import com.darko.ebanckingbackend.repositories.BankAccountRepo;
import com.darko.ebanckingbackend.repositories.CustomerRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.stream.Stream;

@SpringBootApplication
public class EbanckingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EbanckingBackendApplication.class, args);
	}

	@Bean
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

				currentAccount.setBalance(Math.random()*9000);
				currentAccount.setCreatedAt(new Date());
				currentAccount.setStatus(AccountStatus.CREATED);
				currentAccount.setCustomer(cust);
				currentAccount.setOverDraft(90000);
				bankAccountRepo.save(currentAccount);

				savingAccount.setBalance(Math.random()*9000);
				savingAccount.setCreatedAt(new Date());
				savingAccount.setStatus(AccountStatus.CREATED);
				savingAccount.setCustomer(cust);
				savingAccount.setInterestRate(3.2);
				bankAccountRepo.save(currentAccount);


			});
		};
	}

}
