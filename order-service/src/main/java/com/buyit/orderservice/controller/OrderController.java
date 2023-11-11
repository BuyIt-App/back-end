package com.buyit.orderservice.controller;

import com.buyit.orderservice.client.CustomerClient;
import com.buyit.orderservice.dto.*;
import com.buyit.orderservice.model.Order;
import com.buyit.orderservice.repository.OrderRepo;
import com.buyit.orderservice.service.OrderService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")

public class OrderController {

    private final OrderService orderService;
    private final CustomerClient customerClient;
    private final OrderRepo orderRepo;


    @PostMapping("/create")
    public Order createOrder(@RequestBody OrderDetReq order) throws MessagingException, UnsupportedEncodingException {
        CustomerRes customerRes = customerClient.getCustomerById(order.getCustomerId());
        Long orderid = orderService.createOrder(order,customerRes);
        OrderExchangeReq o = new OrderExchangeReq();
        o.setOrderId(orderid);
        o.setCartId(order.getCartId());
        o.setCartItemId(order.getCartItemId());
        Double checkoutAmount = customerClient.getOrderAmount(o);
        Order placedOrder = orderService.updateAmount(checkoutAmount,o.getOrderId());
        return placedOrder;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderDetails(@PathVariable Long orderId) {
        OrderDto order = orderService.getOrderById(orderId);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDto>> getOrdersByCustomerId(@PathVariable Long customerId) {
        List<OrderDto> orders = orderService.getOrdersByCustomerId(customerId);
        if (orders != null) {
            return ResponseEntity.ok(orders);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }


    @PutMapping("/{orderId}")
    public String deleteOrder(@PathVariable long orderId) throws MessagingException, UnsupportedEncodingException {
        return orderService.deleteOrder(orderId);
    }

    @PutMapping("/updateShippingAddress/{orderId}")
    public ResponseEntity<?> updateShippingAddress(@RequestBody String address,@PathVariable long orderId){
        try{
            Order order = orderService.updateShippingAddress(address,orderId);
            return ResponseEntity.ok(order);
        }catch (Exception e){
            return new ResponseEntity<>( e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }


    @PutMapping("/updateDeliveryStatus")
    public String updateDeliveryStatus(@RequestBody OrderStsUpdateReq orderStsUpdateReq) throws MessagingException, UnsupportedEncodingException {
        orderService.updateDeliveryStatus(orderStsUpdateReq);
        return "Delivery status updated successfully";
    }

    @PutMapping("/updateOrderStatus/{orderId}")
    public String updateOrderStatus(@RequestBody String status ,@PathVariable long orderId) throws MessagingException, UnsupportedEncodingException {
        Order order = orderRepo.findById(orderId).get();
        CustomerRes customerRes = customerClient.getCustomerById(order.getCustomerId());
        OtherOrderStatusUpdate otherOrderStatusUpdate = new OtherOrderStatusUpdate(orderId,status,customerRes);
        orderService.orderStatusUpdate(otherOrderStatusUpdate);
        return "Order status updated successfully";
    }



}
