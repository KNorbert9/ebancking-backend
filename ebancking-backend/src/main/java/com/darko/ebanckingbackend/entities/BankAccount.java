package com.darko.ebanckingbackend.entities;

import com.darko.ebanckingbackend.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Entity @DiscriminatorColumn(name = "TYPE", length = 4)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class BankAccount {

    @Id
    private String id;

    private double balance;

    private Date CreatedAt;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "bankAccount")
    private List<AccountOperation> accountOperations;
}
