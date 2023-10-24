package com.darko.ebanckingbackend.entities;

import com.darko.ebanckingbackend.enums.AccountStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Entity @DiscriminatorColumn(name = "TYPE", length = 4)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class BankAccount {

    @Id
    private String id;

    private double balance;

    private Date createdAt;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY)
    private List<AccountOperation> accountOperations;

    @PrePersist
    public void prePersist() {
        createdAt = new Date();
    }
}
