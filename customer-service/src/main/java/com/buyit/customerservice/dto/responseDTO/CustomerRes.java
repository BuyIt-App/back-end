package com.buyit.customerservice.dto.responseDTO;

import com.buyit.customerservice.model.Order;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class CustomerRes {
    private long customerId;
    private String firstName;
    private String lastName;
    private String emailId;
    private Integer phoneNumber;
    private List<Order> orders = new ArrayList<>();


}
