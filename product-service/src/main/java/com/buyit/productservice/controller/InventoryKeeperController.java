package com.buyit.productservice.controller;

import com.buyit.productservice.Service.InventoryKeeperService;
import com.buyit.productservice.dto.InventoryKeeperDto;
import com.buyit.productservice.model.InventoryKeeper;
import com.buyit.productservice.repository.InventoryKeeperRepo;
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
@RequestMapping("/product/inventorykeeper")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class InventoryKeeperController {

    private final AuthenticationService authenticationService;
    private final InventoryKeeperRepo inventoryKeeperRepo;
    private final InventoryKeeperService inventoryKeeperService;

    Logger LOGGER = LoggerFactory.getLogger(InventoryKeeperController.class);

    @PostMapping("/auth/sign_up")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        LOGGER.info("IK sign up");
        if (!userAlreadyExists(request.getEmailId())) {
            try {
                AuthenticationResponse response = authenticationService.register(request);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                LOGGER.error("Error occurred during registration", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
            }
        } else {
            return ResponseEntity.badRequest().body("IK already exists");
        }
    }

    public boolean userAlreadyExists(String email) {
        LOGGER.info("Check if IK already exists");
        Optional<InventoryKeeper> existingUser = inventoryKeeperRepo.findByEmailId(email);
        return existingUser.isPresent();
    }
    @PostMapping("/auth/sign_in")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        LOGGER.info("IK sign in");

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
            if (principal instanceof InventoryKeeper) {
                InventoryKeeper customer = (InventoryKeeper) principal;
                String userRole = customer.getRole();
                return ResponseEntity.ok(userRole);
            }
        }
        LOGGER.warn("Unauthorized request");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<InventoryKeeperDto>> getAllInventoryKeepers() {
        List<InventoryKeeperDto> inventoryKeepers = inventoryKeeperService.getAllInventoryKeepers();
        if (inventoryKeepers.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(inventoryKeepers, HttpStatus.OK);
    }

    @PutMapping("/update/{ikId}")
    public ResponseEntity<InventoryKeeperDto> updateIk(@RequestBody InventoryKeeperDto inventoryKeeperDto,@PathVariable long ikId) {
        InventoryKeeperDto updatedIk = inventoryKeeperService.updateIk(inventoryKeeperDto,ikId);
        return new ResponseEntity<>(updatedIk, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getIkById(@PathVariable long id) {
        try {
            InventoryKeeperDto inventoryKeeper = inventoryKeeperService.getByIkId(id);
            return new ResponseEntity<>(inventoryKeeper, HttpStatus.OK);
        } catch (InventoryKeeperNotFoundException e) {
            return new ResponseEntity<>("Inventory Keeper not found", HttpStatus.NOT_FOUND);
        }
    }

}
