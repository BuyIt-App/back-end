package com.buyit.productservice.model;

import com.buyit.productservice.model.Product;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Product product;
    private int quantity;
    private double subtotal;
}
