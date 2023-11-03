package com.buyit.customerservice.client;

import com.buyit.customerservice.model.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange
public interface OrderClient {
    @GetExchange("/order/customer/{customerId}")
    public List<Order> getOrdersByCustomerId(@PathVariable Long customerId);
}
