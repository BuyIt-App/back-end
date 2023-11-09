package com.buyit.customerservice.repository;

import com.buyit.customerservice.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem,Long> {
    List<CartItem> findByCartId(long id);
}
