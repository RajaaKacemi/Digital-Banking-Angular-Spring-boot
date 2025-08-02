package com.enset.digitalbanking;

import com.enset.digitalbanking.dto.BankAccountDTO;
import com.enset.digitalbanking.dto.CurrentAccountDTO;
import com.enset.digitalbanking.dto.CustomerDTO;
import com.enset.digitalbanking.dto.SavingBankAccountDTO;
import com.enset.digitalbanking.entities.*;
import com.enset.digitalbanking.enums.AccountStatus;
import com.enset.digitalbanking.enums.OperationType;
import com.enset.digitalbanking.exceptions.BalanceNotSufficientException;
import com.enset.digitalbanking.exceptions.BankAccountNotFoundException;
import com.enset.digitalbanking.exceptions.CustomerNotFoundException;
import com.enset.digitalbanking.repositories.AccountOperationRepository;
import com.enset.digitalbanking.repositories.BankAccountRepository;
import com.enset.digitalbanking.repositories.CustomerRepository;
import com.enset.digitalbanking.services.BankAccountService;
import com.enset.digitalbanking.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankingApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
      return args -> {
          Stream.of("Harry", "Potter").forEach(name -> {
              CustomerDTO customer = new CustomerDTO();
              customer.setName(name);
              customer.setEmail("potter" + name + "@gmail.com");
              bankAccountService.saveCustomer(customer);
          });
          bankAccountService.listCustomer().forEach(customer -> {
              try {
                  bankAccountService.saveCurrentBankAccount(Math.random() * 90000, 9000, customer.getId());
                  bankAccountService.saveSavingBankAccount(Math.random()*120000, 5.5, customer.getId());

              } catch (CustomerNotFoundException e) {
                  e.printStackTrace();
              }
          });

          List<BankAccountDTO> bankAccountDTOs = bankAccountService.bankAccountList();
          for(BankAccountDTO bankAccountDTO : bankAccountDTOs) {
              for (int i = 0; i < 10; i++) {

                  bankAccountService.debit(bankAccountDTO.getId(), 1000+Math.random() * 1000, "Debit");
                  bankAccountService.credit(bankAccountDTO.getId(), 1000+Math.random() * 12000, "Credit");
              }
          }
      };
    };

    CommandLineRunner start(CustomerRepository customerRepository, BankAccountRepository bankAccountRepository, AccountOperationRepository accountOperationRepository) {
        return args -> {
            Stream.of("Hassan", "Yassine", "Aicha").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(cust->{
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random()*90000);
                currentAccount.setCustomer(cust);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random()*90000);
                savingAccount.setCustomer(cust);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);

            });
            bankAccountRepository.findAll().forEach(acc->{
                for (int i = 0; i <10 ; i++) {
                    AccountOperation accountOperation=new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random()*12000);
                    accountOperation.setType(Math.random()>0.5? OperationType.DEBIT: OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }

            });
        };
    };

}
