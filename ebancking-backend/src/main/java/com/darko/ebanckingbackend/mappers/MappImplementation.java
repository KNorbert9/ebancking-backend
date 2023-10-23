package com.darko.ebanckingbackend.mappers;

import com.darko.ebanckingbackend.dtos.CustomerDTO;
import com.darko.ebanckingbackend.entities.Customer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


@Service
public class MappImplementation {

    public CustomerDTO FromCustomer(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();

        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

    public Customer FromCustomerDTO(CustomerDTO customerDTO){
        Customer customer = new Customer();

        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }
}
