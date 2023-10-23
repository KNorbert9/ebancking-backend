package com.darko.ebanckingbackend.dtos;

import com.darko.ebanckingbackend.enums.AccountStatus;
import jakarta.persistence.PrePersist;
import lombok.Data;

import java.util.Date;

@Data
public class CurrentBankAccountDTO extends BankAccountDTO{

    private String id;

    private double balance;

    private Date createdAt;

    private AccountStatus status;

    private CustomerDTO customerDTO;

    private double overDraft;


    @PrePersist
    public void prePersist() {
        createdAt = new Date();
    }
}
