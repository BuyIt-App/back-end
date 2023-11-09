package com.buyit.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryKeeperDto {
    private long inventoryKeeperId;
    private String firstName;
    private String lastName;
    private String emailId;
    private Integer phoneNumber;
    private String address;
}
