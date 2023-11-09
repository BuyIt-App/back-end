package com.buyit.orderservice.client;

import com.buyit.orderservice.dto.CustomerRes;
import com.buyit.orderservice.dto.OrderExchangeReq;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface CustomerClient {

    @GetExchange("/customer/{id}")
    public CustomerRes getCustomerById(@PathVariable long id);

    @GetExchange("/customer/cart/getOrderAmount")
    public Double getOrderAmount(@RequestBody OrderExchangeReq orderExchangeReq);
}
