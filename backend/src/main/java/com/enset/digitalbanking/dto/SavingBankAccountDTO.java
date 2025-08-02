package com.enset.digitalbanking.dto;

import lombok.Data;

@Data
public class SavingBankAccountDTO extends BankAccountDTO {

    private String id;
    private double intrestRate;
    private CustomerDTO customerDTO;
}
