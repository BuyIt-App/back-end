package com.buyit.customerservice.repository;

import com.buyit.customerservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByResetPasswordToken(String token);
    Optional<Customer> findByEmailId(String emailId);

}
