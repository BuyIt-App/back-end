package com.buyit.deliveryservice.controller;

import com.buyit.deliveryservice.client.OrderClient;
import com.buyit.deliveryservice.dto.DeliverPersonRes;
import com.buyit.deliveryservice.dto.OrderStsUpdateReq;
import com.buyit.deliveryservice.model.DeliveryPerson;
import com.buyit.deliveryservice.repository.DeliveryPersonRepo;
import com.buyit.deliveryservice.service.DeliveryPersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
@Slf4j
public class DeliveryPersonController {
    private final AuthenticationService authenticationService;
    private final DeliveryPersonRepo deliveryPersonRepo;
    private final DeliveryPersonService deliveryPersonService;
    @Autowired
            private OrderClient orderClient;


    Logger LOGGER = LoggerFactory.getLogger(DeliveryPersonController.class);

    @PostMapping("/sign_up")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        LOGGER.info("User sign up");
        if (!userAlreadyExists(request.getEmailId())) {
            try {
                AuthenticationResponse response = authenticationService.register(request);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                LOGGER.error("Error occurred during registration", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
            }
        } else {
            return ResponseEntity.badRequest().body("User already exists");
        }
    }
    public boolean userAlreadyExists(String email) {
        LOGGER.info("Check if user already exists");
        Optional<DeliveryPerson> existingUser = deliveryPersonRepo.findByEmailId(email);
        return existingUser.isPresent();
    }

    @PostMapping("/sign_in")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        LOGGER.info("User sign in");
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping("/role")
    public ResponseEntity<String> getUserRole(Authentication authentication) {
        LOGGER.info("Get user role");
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof DeliveryPerson) {
                DeliveryPerson deliveryPerson = (DeliveryPerson) principal;
                String userRole = deliveryPerson.getRole();
                return ResponseEntity.ok(userRole);
            }
        }
        LOGGER.warn("Unauthorized request");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/id")
    public ResponseEntity<Long> getUserId(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof DeliveryPerson) {
                DeliveryPerson user = (DeliveryPerson) principal;
                Long userId = user.getId();
                return ResponseEntity.ok(userId);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    @GetMapping("/all")
    public List<DeliverPersonRes> getAllDeliveryPersons() {
        return deliveryPersonService.getAllDeliverPersons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDeliveryPersonById(@PathVariable long id) {
        DeliverPersonRes deliverPerson = deliveryPersonService.findById(id);

        if (deliverPerson != null) {
            return ResponseEntity.ok(deliverPerson);
        } else {
            String notFoundMessage = "Delivery person not found.";
            return new ResponseEntity<>(notFoundMessage, HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/{deliveryPersonId}/pickOrder/{orderid}")
    private String pickOrder(@PathVariable long deliveryPersonId, @PathVariable long orderid){
        OrderStsUpdateReq or = new OrderStsUpdateReq();
        or.setDeliveryPersonId(deliveryPersonId);
        or.setStatus("Out for Delivery");
        or.setOrderId(orderid);
        return orderClient.updateDeliveryStatus(or);
    }

}
