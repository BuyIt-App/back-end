package com.buyit.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDetReq {
    private Integer customerId;
    private List<Long> cartItemId;
    private long cartId;
    private String shippingAddress;


}
