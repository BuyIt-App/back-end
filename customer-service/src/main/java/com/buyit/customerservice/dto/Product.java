package com.buyit.customerservice.dto;

import com.buyit.customerservice.model.Cart;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
    private long id;
    private String productName;
    private String description;
    private long quantity;
    private int price;

}