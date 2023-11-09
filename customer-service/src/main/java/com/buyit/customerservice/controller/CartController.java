package com.buyit.customerservice.controller;

import com.buyit.customerservice.client.OrderClient;
import com.buyit.customerservice.client.ProductClient;
import com.buyit.customerservice.dto.ProductRes;
import com.buyit.customerservice.dto.requestDTO.OrderExchangeReq;
import com.buyit.customerservice.dto.responseDTO.CartRes;
import com.buyit.customerservice.model.Cart;
import com.buyit.customerservice.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer/cart")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService;
    private final OrderClient orderClient;
    private final ProductClient productClient;

    Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @PostMapping("/{cartId}/add")
    public ResponseEntity<?> addToCart(@PathVariable Long cartId, @RequestParam Long productId, @RequestParam int quantity) {
        LOGGER.info("Add product to cart");
        ProductRes product = productClient.getProductById(productId);

        if (product == null) {
            LOGGER.warn("Product not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        if (product.getQuantity().equals("Out of stock")) {
            LOGGER.warn("Product is out of stock");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product is out of stock");
        }

        Cart cart = cartService.getCart(cartId);

        cartService.addToCart(cart, product, quantity);

        LOGGER.info("Product added to cart successfully");
        return ResponseEntity.status(HttpStatus.OK).body("Product added to the cart successfully");
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<?> getCart(@PathVariable Long cartId) {
        LOGGER.info("Get cart by ID");
        try {
            CartRes cart = cartService.getCartRes(cartId);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            LOGGER.error("Error occurred while retrieving the cart", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @GetMapping("/getOrderAmount")
    public Double getOrderAmount(@RequestBody OrderExchangeReq orderExchangeReq){
        return cartService.getCheckOutAmount(orderExchangeReq);
    }

    @GetMapping("/cart/refreshCart/{cartId}")
    public String refreshCart(@PathVariable long cartId){
        return refreshCart(cartId);
    }


    @DeleteMapping("/deleteCartItems/{cartId}")
    public ResponseEntity<String> deleteCartItems(@PathVariable long cartId, @RequestParam List<Long> cartItemIds) {
        try {
            cartService.deleteCartItems(cartItemIds,cartId);
            return new ResponseEntity<>("Cart items deleted successfully", HttpStatus.OK);
        } catch (Exception e) {

            return new ResponseEntity<>("Error deleting cart items: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
