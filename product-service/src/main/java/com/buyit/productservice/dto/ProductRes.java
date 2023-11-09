package com.buyit.productservice.dto;

import com.buyit.productservice.model.CartItem;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductRes {
    private long productId;
    private String productName;
    private String description;
    private String quantity;
    private BigDecimal price;
    private String category;

}
