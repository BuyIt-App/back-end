package com.buyit.customerservice.controller;

import com.buyit.customerservice.client.OrderClient;
import com.buyit.customerservice.client.ProductClient;
import com.buyit.customerservice.dto.Product;
import com.buyit.customerservice.dto.ProductRes;
import com.buyit.customerservice.dto.requestDTO.OrderDetReq;
import com.buyit.customerservice.dto.requestDTO.OrderExchangeReq;
import com.buyit.customerservice.dto.requestDTO.OrderReq;
import com.buyit.customerservice.dto.responseDTO.CartRes;
import com.buyit.customerservice.dto.responseDTO.CustomerRes;
import com.buyit.customerservice.model.*;
import com.buyit.customerservice.repository.CustomerRepo;
import com.buyit.customerservice.service.CartService;
import com.buyit.customerservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CustomerController {
    private final AuthenticationService authenticationService;
    private final CustomerRepo customerRepo;
    private final CustomerService customerService;
    private final CartService cartService;
    private final OrderClient orderClient;
    private final ProductClient productClient;

    Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @PostMapping("/auth/sign_up")
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
    @PostMapping("/auth/sign_in")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        LOGGER.info("User sign in");

        try {
            // Attempt to authenticate the user
            AuthenticationResponse response = authenticationService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            // Handle authentication error and set an error message
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthenticationResponse.builder().message("Incorrect username or password").build());
        }
    }


    @GetMapping("/role")
    public ResponseEntity<String> getUserRole(Authentication authentication) {
        LOGGER.info("Get user role");
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Customer) {
                Customer customer = (Customer) principal;
                String userRole = customer.getRole();
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
            if (principal instanceof Customer) {
                Customer user = (Customer) principal;
                Long userId = user.getCustomerId();
                return ResponseEntity.ok(userId);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }



    @GetMapping("/{id}")
    public CustomerRes getCustomerById(@PathVariable long id){
        return customerService.findById(id);
    }

    public boolean userAlreadyExists(String email) {
        LOGGER.info("Check if user already exists");
        Optional<Customer> existingUser = customerRepo.findByEmailId(email);
        return existingUser.isPresent();
    }

    @GetMapping("/all")
    public ResponseEntity<List<CustomerRes> >getAllCustomers() {
        LOGGER.info("Get all customers");
        List<CustomerRes> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

//    @GetMapping("/with-orders")
//    public List<CustomerRes> getAllCustomersWithOrder() {
//        LOGGER.info("Get all customers with orders");
//        List<CustomerRes> customers = customerService.getAllCustomers();
//        customers.forEach(customer -> customer.setOrderList(orderClient.getOrdersByCustomerId(customer.getCustomerId())));
//        return customers;
//    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerRes> updateCustomer(@RequestBody CustomerRes customerRes,@PathVariable long customerId) {
        CustomerRes updatedCustomer = customerService.updateCustomer(customerRes,customerId);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }






}
