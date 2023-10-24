package com.darko.ebanckingbackend.web;


import com.darko.ebanckingbackend.dtos.AccountHistoryDTO;
import com.darko.ebanckingbackend.dtos.AccountOperationDTO;
import com.darko.ebanckingbackend.dtos.BankAccountDTO;
import com.darko.ebanckingbackend.exceptions.BankAccountNotFoundException;
import com.darko.ebanckingbackend.services.BanckAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BankAccountController {

    private BanckAccountService banckAccountService;

    public BankAccountController(BanckAccountService banckAccountService) {
        this.banckAccountService = banckAccountService;
    }


    @GetMapping("/accounts/{id}")
    public BankAccountDTO getBankAccount(@PathVariable String id) throws BankAccountNotFoundException {
        return banckAccountService.getBankAccount(id);
    }


    @GetMapping("/accounts")
    public List<BankAccountDTO> bankAccountDTOList(){
        return banckAccountService.listBankAccount();
    }


    @GetMapping("/accounts/{id}/operations")
    public List<AccountOperationDTO> listOperation(@PathVariable String id){
        return banckAccountService.accountHistory(id);
    }

    @GetMapping("/accounts/{id}/pageOperations")
    public AccountHistoryDTO getHistoryDTO(
            @PathVariable String id,
            @RequestParam(name = "page",defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) throws BankAccountNotFoundException {
        return banckAccountService.getAccountHistory(id, page, size);
    }
}
