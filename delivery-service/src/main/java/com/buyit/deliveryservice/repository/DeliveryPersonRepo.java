package com.buyit.deliveryservice.repository;

import com.buyit.deliveryservice.model.DeliveryPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryPersonRepo extends JpaRepository<DeliveryPerson,Long> {
    Optional<DeliveryPerson> findByResetPasswordToken(String token);
    Optional<DeliveryPerson> findByEmailId(String emailId);
}
