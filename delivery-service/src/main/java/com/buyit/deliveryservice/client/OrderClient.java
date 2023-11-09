package com.buyit.deliveryservice.client;

import com.buyit.deliveryservice.dto.OrderStsUpdateReq;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PutExchange;

import java.util.List;

@HttpExchange
public interface OrderClient {
    @PutExchange("/order/updateDeliveryStatus")
    public String updateDeliveryStatus(@RequestBody OrderStsUpdateReq orderStsUpdateReq);

}
