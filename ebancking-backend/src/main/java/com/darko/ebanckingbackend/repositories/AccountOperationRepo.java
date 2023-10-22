package com.darko.ebanckingbackend.repositories;

import com.darko.ebanckingbackend.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepo extends JpaRepository<AccountOperation, Long> {
}
