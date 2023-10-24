package com.darko.ebanckingbackend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AccountHistoryDTO {

    private String accountId;

    private List<AccountOperationDTO> listOperations;

    private double balance;

    private String accountType;

    private int currentPage;

    private int totalPage;

    private int pageSize;
}
