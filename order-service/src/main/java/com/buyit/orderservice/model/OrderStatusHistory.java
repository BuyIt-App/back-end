package com.buyit.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_status_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderStatusHistoryId;
    private long orderId;
    private String status;
    private LocalDateTime timestamp;



}
