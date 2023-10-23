package com.darko.ebanckingbackend.web;

import com.darko.ebanckingbackend.dtos.CustomerDTO;
import com.darko.ebanckingbackend.entities.Customer;
import com.darko.ebanckingbackend.services.BanckAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class CustomerController {

    private BanckAccountService banckAccountService;

    @GetMapping("/Customers")
    public List<CustomerDTO> listCustomer(){
        return banckAccountService.listCustomer();
    }
}
