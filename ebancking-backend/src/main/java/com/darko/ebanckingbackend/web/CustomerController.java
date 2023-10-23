package com.darko.ebanckingbackend.web;

import com.darko.ebanckingbackend.dtos.CustomerDTO;
import com.darko.ebanckingbackend.entities.Customer;
import com.darko.ebanckingbackend.services.BanckAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/customer")
public class CustomerController {

    private BanckAccountService banckAccountService;

    @GetMapping("/Customers")
    public List<CustomerDTO> listCustomer(){
        return banckAccountService.listCustomer();
    }


    @GetMapping("Customer/{id}")
    public CustomerDTO getCustomerById(@PathVariable(name = "id") Long id){
        return banckAccountService.getCostomerDTOById(id);
    }

    @PostMapping("/addCustomer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return banckAccountService.saveCustomer(customerDTO);
    }

    @PutMapping("/update/{id}")
    public CustomerDTO updateCustomer(@PathVariable("id") Long id, @RequestBody CustomerDTO customerDTO){
        customerDTO.setId(id);
        return banckAccountService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCustomer(@PathVariable("id") Long id){
        banckAccountService.deleteCustomer(id);
    }
}
