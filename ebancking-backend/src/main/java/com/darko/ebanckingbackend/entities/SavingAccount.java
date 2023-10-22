package com.darko.ebanckingbackend.entities;

import jakarta.persistence.DiscriminatorValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("SA")
public class SavingAccount extends BankAccount{

    public double interestRate;
}
