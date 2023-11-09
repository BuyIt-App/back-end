package com.buyit.customerservice.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class OrderExchangeReq {
    private List<Long> cartItemId;
    private long cartId;
    private long orderId;

}
