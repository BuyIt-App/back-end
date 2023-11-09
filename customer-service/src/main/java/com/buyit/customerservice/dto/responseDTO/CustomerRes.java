package com.buyit.customerservice.dto.responseDTO;

import com.buyit.customerservice.model.Cart;
import com.buyit.customerservice.dto.OrderDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class CustomerRes {
    private long customerId;
    private String firstName;
    private String lastName;
    private String emailId;
    private Integer phoneNumber;

}
