package com.darko.ebanckingbackend.dtos;

import com.darko.ebanckingbackend.entities.AccountOperation;
import com.darko.ebanckingbackend.entities.Customer;
import com.darko.ebanckingbackend.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SavingBankAccountDTO extends BankAccountDTO{

    private String id;

    private double balance;

    private Date createdAt;

    private AccountStatus status;

    private CustomerDTO customerDTO;

    private double interestRate;


    @PrePersist
    public void prePersist() {
        createdAt = new Date();
    }
}
