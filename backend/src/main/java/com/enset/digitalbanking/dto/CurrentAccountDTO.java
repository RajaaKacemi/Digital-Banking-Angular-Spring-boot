package com.enset.digitalbanking.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrentAccountDTO extends BankAccountDTO {

    private String id;
    private BigDecimal interestRate;
    private CustomerDTO customerDTO;
}