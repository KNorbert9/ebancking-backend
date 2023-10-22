package com.darko.ebanckingbackend.repositories;

import com.darko.ebanckingbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
}
