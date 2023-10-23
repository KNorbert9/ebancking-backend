package com.darko.ebanckingbackend.dtos;

import com.darko.ebanckingbackend.entities.BankAccount;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
public class CustomerDTO {

    private Long id;

    private String name;

    private String email;

}
