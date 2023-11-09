package com.buyit.orderservice.model;

import com.buyit.orderservice.dto.CartItemDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "order_det")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;
    private Integer customerId;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private String shippingAddress;
    private long deliveryPersonId ;


}
