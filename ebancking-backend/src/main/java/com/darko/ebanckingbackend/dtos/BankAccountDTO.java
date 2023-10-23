package com.darko.ebanckingbackend.dtos;

import com.darko.ebanckingbackend.entities.AccountOperation;
import com.darko.ebanckingbackend.entities.Customer;
import com.darko.ebanckingbackend.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
public class BankAccountDTO {

    private String id;

    private double balance;

    private Date createdAt;

    private AccountStatus status;

    private Customer customer;

    private List<AccountOperation> accountOperations;

    @PrePersist
    public void prePersist() {
        createdAt = new Date();
    }
}
