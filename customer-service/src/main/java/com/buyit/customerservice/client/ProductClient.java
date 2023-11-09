package com.buyit.customerservice.client;

import com.buyit.customerservice.dto.Product;
import com.buyit.customerservice.dto.ProductRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PutExchange;

@HttpExchange
public interface ProductClient {
    @GetExchange("/product/{productId}")
    public ProductRes getProductById(@PathVariable long productId);
    @PutExchange("/product/{productId}/updateQuantity/{quantity}")
    public void updateProductQuantity(@PathVariable long productId, @PathVariable long quantity);

}
