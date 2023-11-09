package com.buyit.orderservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class OrderStsUpdateReq {
    private long orderId;
    private String status;
    private long deliveryPersonId;
}
