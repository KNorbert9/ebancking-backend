package com.darko.ebanckingbackend.web;


import com.darko.ebanckingbackend.services.BanckAccountService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankAccountController {

    private BanckAccountService banckAccountService;

    public BankAccountController(BanckAccountService banckAccountService) {
        this.banckAccountService = banckAccountService;
    }
}
