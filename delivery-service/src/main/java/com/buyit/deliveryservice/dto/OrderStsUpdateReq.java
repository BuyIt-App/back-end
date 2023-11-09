package com.buyit.deliveryservice.dto;

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
