package com.buyit.customerservice.controller;

import com.buyit.customerservice.client.OrderClient;
import com.buyit.customerservice.dto.responseDTO.CustomerRes;
import com.buyit.customerservice.model.Customer;
import com.buyit.customerservice.model.Order;
import com.buyit.customerservice.repository.CustomerRepo;
import com.buyit.customerservice.service.CustomerService;
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
@RequestMapping("/customer")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {
    private final AuthenticationService authenticationService;
    private final CustomerRepo customerRepo;
    private final CustomerService customerService;

    @Autowired
    private OrderClient orderClient;

    Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);


    @PostMapping("/sign_up")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {
        LOGGER.info("User sign up");
        if(!userAlreadyExists(request.getEmailId())) {
            try {
                AuthenticationResponse response = authenticationService.register(request);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
            }
        }
        else {
            return ResponseEntity.badRequest().body("User already exists");
        }
    }

    @PostMapping("/sign_in")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping("/role")
    public ResponseEntity<String> getUserRole(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Customer) {
                Customer customer = (Customer) principal;
                String userRole = customer.getRole();
                return ResponseEntity.ok(userRole);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/id")
    public ResponseEntity<Long> getUserId(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Customer) {
                Customer customer = (Customer) principal;
                Long userId = customer.getCustomerId();
                return ResponseEntity.ok(userId);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public boolean userAlreadyExists(String email) {
        Optional<Customer> existingUser = customerRepo.findByEmailId(email);
        return existingUser.isPresent();
    }

    @GetMapping("/all")
    public ResponseEntity<List<CustomerRes>> getAllCustomers() {
        List<CustomerRes> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/with-orders")
    public List<CustomerRes> getAllCustomersWithOrder() {
        List<CustomerRes> customers = customerService.getAllCustomers();
        customers.forEach(customer -> customer.setOrders(orderClient.getOrdersByCustomerId(customer.getCustomerId())));
        return customers;
    }



}
