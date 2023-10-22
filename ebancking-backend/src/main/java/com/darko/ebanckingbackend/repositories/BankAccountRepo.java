package com.darko.ebanckingbackend.repositories;

import com.darko.ebanckingbackend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepo extends JpaRepository<BankAccount, String> {
}
