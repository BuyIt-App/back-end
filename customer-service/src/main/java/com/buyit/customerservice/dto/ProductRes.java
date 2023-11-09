package com.buyit.customerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductRes {
    private long productId;
    private String productName;
    private String description;
    private String quantity;
    private int price;
    private String category;

}
