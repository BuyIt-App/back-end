package com.buyit.productservice.repository;

import com.buyit.productservice.model.InventoryKeeper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryKeeperRepo  extends JpaRepository<InventoryKeeper, Long> {
    Optional<InventoryKeeper> findByEmailId(String emailId);

}
