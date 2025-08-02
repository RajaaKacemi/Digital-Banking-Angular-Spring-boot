package com.enset.digitalbanking.repositories;

import com.enset.digitalbanking.entities.BankAccount;
import com.enset.digitalbanking.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
