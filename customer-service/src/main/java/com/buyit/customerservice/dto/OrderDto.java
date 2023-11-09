package com.buyit.customerservice.dto;

import com.buyit.customerservice.model.CartItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.Timestamp;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDto {
    private long orderId;
    private long customerId;
    private Timestamp orderDate;
    private String totalAmount;
    private String status;

}
