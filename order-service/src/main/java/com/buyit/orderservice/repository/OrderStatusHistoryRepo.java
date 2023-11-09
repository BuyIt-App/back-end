package com.buyit.orderservice.repository;

import com.buyit.orderservice.model.Order;
import com.buyit.orderservice.model.OrderStatusHistory;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface OrderStatusHistoryRepo extends JpaRepository<OrderStatusHistory,Long> {
    List<OrderStatusHistory> findByOrderId(Long orderid);
}
