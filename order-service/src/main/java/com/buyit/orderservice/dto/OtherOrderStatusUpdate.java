package com.buyit.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OtherOrderStatusUpdate {
    private long orderId;
    private String status;
    private CustomerRes customerRes;

}
