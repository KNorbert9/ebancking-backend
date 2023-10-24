package com.darko.ebanckingbackend.dtos;

import com.darko.ebanckingbackend.entities.BankAccount;
import com.darko.ebanckingbackend.enums.OperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
public class AccountOperationDTO {

    private Long id;

    private Date createdAt;

    private Double amount;

    private OperationType Type;

    private String description;

}
