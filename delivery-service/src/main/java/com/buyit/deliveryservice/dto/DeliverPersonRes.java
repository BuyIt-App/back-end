package com.buyit.deliveryservice.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DeliverPersonRes {
    private long deliveryPersonId;
    private String firstName;
    private String lastName;
    private String emailId;
    private Integer phoneNumber;
    private String vehicleNumber;

}
