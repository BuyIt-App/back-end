package com.buyit.customerservice.client;

import com.buyit.customerservice.dto.OrderDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange
public interface OrderClient {
    @GetExchange("/order/customer/{customerId}")
    public List<OrderDto> getOrdersByCustomerId(@PathVariable Long customerId);
}
