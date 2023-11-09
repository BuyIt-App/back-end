package com.buyit.orderservice.dto;

import jakarta.persistence.*;
import lombok.*;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class OrderDto {
    private long orderId;
    private Integer customerId;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private String shippingAddress;

    private List<OrderStatusDto> orderStatusDtos;
    private long deliveryPersonId;

}
