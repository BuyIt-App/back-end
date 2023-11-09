package com.buyit.customerservice.dto.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderReq {
    private Integer customerId;
    private List<Long> cartItemId;
    private long cartId;

}
