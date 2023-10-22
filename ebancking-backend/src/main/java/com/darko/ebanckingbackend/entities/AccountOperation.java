package com.darko.ebanckingbackend.entities;

import com.darko.ebanckingbackend.enums.OperationType;
import jakarta.persistence.*;
import jdk.dynalink.Operation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data @AllArgsConstructor
@NoArgsConstructor @Entity
public class AccountOperation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date createdAt;

    private Double amount;

    private OperationType Type;

    @ManyToOne
    private BankAccount bankAccount;
}
