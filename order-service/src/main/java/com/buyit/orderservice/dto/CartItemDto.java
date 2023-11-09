package com.buyit.orderservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class CartItemDto {

    private Long cartItemId;
    private long productId;
    private int quantity;
    private double subtotal;
}
