package com.buyit.orderservice.service;

import com.buyit.orderservice.client.CustomerClient;
import com.buyit.orderservice.dto.*;
import com.buyit.orderservice.mail.EmailSender;
import com.buyit.orderservice.model.Order;
import com.buyit.orderservice.model.OrderStatusHistory;
import com.buyit.orderservice.repository.OrderRepo;
import com.buyit.orderservice.repository.OrderStatusHistoryRepo;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final OrderStatusHistoryRepo orderStatusHistoryRepo;
    private final JavaMailSender mailSender;
    @Autowired
    private CustomerClient customerClient;


    public Long createOrder(OrderDetReq orderDetReq, CustomerRes customerRes) throws MessagingException, UnsupportedEncodingException {
        String email = customerRes.getEmailId();
        Order order = new Order();
        LocalDateTime currentDateTime = LocalDateTime.now();
        order.setOrderDate(currentDateTime);
        order.setShippingAddress(orderDetReq.getShippingAddress());
        order.setCustomerId(orderDetReq.getCustomerId());
        orderRepo.save(order);
        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setOrderId(order.getOrderId());
        orderStatusHistory.setStatus("Pending");
        orderStatusHistory.setTimestamp(currentDateTime);
        orderStatusHistoryRepo.save(orderStatusHistory);

        EmailSender emailSender = new EmailSender(mailSender);
        String subject = "Order "+"1652476990bu"+ order.getOrderId()+ "Confirmation";
        String content = "<html><body style='background-color: #f3f3f3; font-family: Arial, sans-serif;'>"
                + "<div style='background-color: #3498db; color: white; padding: 10px; text-align: center;'>"
                + "<h2> "+customerRes.getFirstName() +", Your Order Confirmation</h2></div>"
                + "<div style='padding: 20px;'>"
                + "<p>Thank you for placing your order with Buyit.</p>"
                + "<p><strong>Order placed on:</strong> " + order.getOrderDate() + "</p>"
                + "<p><strong>Order ID:</strong> " + "1652476990bu" + order.getOrderId() + "</p>"
                // Add more order details here
                + "</div>"
                + "</body></html>";


        emailSender.sendEmail(email, subject, content);

        orderStatusHistory.setStatus("Confirmed");
        orderStatusHistory.setTimestamp(currentDateTime);
        orderStatusHistoryRepo.save(orderStatusHistory);

        return order.getOrderId();
    }



    public List<OrderDto> getOrdersByCustomerId(Long customerId) {
        List<Order> orders = orderRepo.findByCustomerId(customerId);
        List<OrderDto> orderDtos = new ArrayList<>();
        for(Order o : orders){
            OrderDto od = new OrderDto();
            od.setCustomerId(o.getCustomerId());
            od.setOrderDate(o.getOrderDate());
            List<OrderStatusDto> orderStatusDtos = getOrderStatusPerOrderId(o.getOrderId());
            od.setOrderStatusDtos(orderStatusDtos);
            od.setOrderId(o.getOrderId());
            od.setShippingAddress(o.getShippingAddress());
            od.setDeliveryPersonId(o.getDeliveryPersonId());
            od.setTotalAmount(o.getTotalAmount());
            orderDtos.add(od);
        }
        return orderDtos;
    }

    public List<OrderStatusDto> getOrderStatusPerOrderId(long orderId){
        List<OrderStatusHistory> orderStatusHistories = orderStatusHistoryRepo.findByOrderId(orderId);
        List<OrderStatusDto> orderStatusDtos = new ArrayList<>();
        for(OrderStatusHistory o : orderStatusHistories){
            OrderStatusDto os = new OrderStatusDto();
            os.setStatus(o.getStatus());
            os.setTimestamp(o.getTimestamp());
            orderStatusDtos.add(os);
        }
        return orderStatusDtos;
    }



    public OrderDto getOrderById(Long orderId) {
        Order o = orderRepo.findById(orderId).get();
        OrderDto od = new OrderDto();
        od.setCustomerId(o.getCustomerId());
        od.setOrderDate(o.getOrderDate());
        List<OrderStatusDto> orderStatusDtos = getOrderStatusPerOrderId(o.getOrderId());
        od.setOrderStatusDtos(orderStatusDtos);
        od.setOrderId(o.getOrderId());
        od.setShippingAddress(o.getShippingAddress());
        od.setDeliveryPersonId(o.getDeliveryPersonId());
        od.setTotalAmount(o.getTotalAmount());

        return od;
    }

    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepo.findAll();
        List<OrderDto> orderDtos = new ArrayList<>();
        for(Order o : orders){
            OrderDto orderDto = getOrderById(o.getOrderId());
            orderDtos.add(orderDto);
        }

        return orderDtos;
    }

    public Order updateAmount(Double checkoutAmount, long orderId) {
        Order order = orderRepo.findById(orderId).get();
        order.setTotalAmount(checkoutAmount);
        return orderRepo.save(order);
    }

    public void updateDeliveryStatus(OrderStsUpdateReq orderStsUpdateReq) throws MessagingException, UnsupportedEncodingException {
        Order order = orderRepo.findById(orderStsUpdateReq.getOrderId()).get();
        order.setOrderId(order.getOrderId());
        order.setCustomerId(order.getCustomerId());
        order.setTotalAmount(order.getTotalAmount());
        order.setOrderDate(order.getOrderDate());
        order.setDeliveryPersonId(orderStsUpdateReq.getDeliveryPersonId());
        orderRepo.save(order);

        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setOrderId(order.getOrderId());
        orderStatusHistory.setStatus(orderStsUpdateReq.getStatus());
        LocalDateTime currentDateTime = LocalDateTime.now();
        orderStatusHistory.setTimestamp(currentDateTime);

        CustomerRes customerRes = customerClient.getCustomerById(order.getCustomerId());
        String email = customerRes.getEmailId();
        EmailSender emailSender = new EmailSender(mailSender);
        String subject = "Order "+"1652476990bu"+ order.getOrderId()+ "Delivery Update";
        String content = "<html><body style='background-color: #f3f3f3; font-family: Arial, sans-serif;'>"
                + "<div style='background-color: #3498db; color: white; padding: 10px; text-align: center;'>"
                + "<h2>Your Order Delivery Update</h2></div>"
                + "<div style='padding: 20px;'>"
                + "<p>Thank you for placing your order with Buyit.</p>"
                + "<p><strong>Order placed on:</strong> " + order.getOrderDate() + "</p>"
                + "<p><strong>Order Delivery accepted on:</strong> " + currentDateTime + "</p>"
                + "<p><strong>Order ID:</strong> " + "1652476990bu" + order.getOrderId() + "</p>"
                // Add more order details here
                + "</div>"
                + "</body></html>";

        emailSender.sendEmail(email, subject, content);

        orderStatusHistoryRepo.save(orderStatusHistory);

    }

    public void orderStatusUpdate(OtherOrderStatusUpdate oosu) throws MessagingException, UnsupportedEncodingException {
        Order order = orderRepo.findById(oosu.getOrderId()).get();
        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.setOrderId(order.getOrderId());
        orderStatusHistory.setStatus(oosu.getStatus());
        LocalDateTime currentDateTime = LocalDateTime.now();
        orderStatusHistory.setTimestamp(currentDateTime);

        String email = oosu.getCustomerRes().getEmailId();
        EmailSender emailSender = new EmailSender(mailSender);
        String subject = "Order "+"1652476990bu"+ order.getOrderId()+ "Delivery Update";
        String content = "<html><body style='background-color: #f3f3f3; font-family: Arial, sans-serif;'>"
                + "<div style='background-color: #3498db; color: white; padding: 10px; text-align: center;'>"
                + "<h2>Your Order Delivery Update</h2></div>"
                + "<div style='padding: 20px;'>"
                + "<p>Thank you for placing your order with Buyit.</p>"
                + "<p><strong>Order placed on:</strong> " + order.getOrderDate() + "</p>"
                + "<p><strong>Order"+ oosu.getStatus() +" on:</strong> " + currentDateTime + "</p>"
                + "<p><strong>Order ID:</strong> " + "1652476990bu" + order.getOrderId() + "</p>"
                // Add more order details here
                + "</div>"
                + "</body></html>";

        if (oosu.getStatus().equals("Canceled")){
            emailSender.sendEmail(email, subject, cancelMailContent(order,oosu,currentDateTime));
        }else{
            emailSender.sendEmail(email, subject, content);
        }




        orderStatusHistoryRepo.save(orderStatusHistory);

    }

    public String deleteOrder(long orderId) throws MessagingException, UnsupportedEncodingException {
        Order order = orderRepo.findById(orderId).orElse(null);
        CustomerRes customerRes = customerClient.getCustomerById(order.getCustomerId());
        OtherOrderStatusUpdate o = new OtherOrderStatusUpdate(orderId," Canceled",customerRes);
        orderStatusUpdate(o);
        return "Order canceled Successfully";
    }

    public String cancelMailContent(Order order, OtherOrderStatusUpdate oosu, LocalDateTime currentDateTime){

        String content = "<html><body style='background-color: #f3f3f3; font-family: Arial, sans-serif;'>"
                + "<div style='background-color: #FF0000; color: white; padding: 10px; text-align: center;'>"
                + "<h2>Sorry, Your Order Has been Canceled</h2></div>"
                + "<div style='padding: 20px;'>"
                + "<p>Due to some reason order has been canceled. Thank you for placing your order with Buyit.</p>"
                + "<p><strong>Order placed on:</strong> " + order.getOrderDate() + "</p>"
                + "<p><strong>Order"+ oosu.getStatus() +" on:</strong> " + currentDateTime + "</p>"
                + "<p><strong>Order ID:</strong> " + "1652476990bu" + order.getOrderId() + "</p>"
                // Add more order details here
                  + "<p><strong>Order ID:</strong> Explore more on buy it.</p>"
                + "</div>"
                + "</body></html>";

        return content;
    }


}

