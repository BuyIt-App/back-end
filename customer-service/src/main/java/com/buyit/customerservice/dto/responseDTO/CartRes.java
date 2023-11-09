package com.buyit.customerservice.dto.responseDTO;

import com.buyit.customerservice.dto.CartItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartRes {
    private Long id;
    private double totalPrice;
    List<CartItemDto> cartItemDtos;
}
