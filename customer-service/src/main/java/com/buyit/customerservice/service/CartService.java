package com.buyit.customerservice.service;

import com.buyit.customerservice.client.ProductClient;
import com.buyit.customerservice.dto.CartItemDto;
import com.buyit.customerservice.dto.ProductRes;
import com.buyit.customerservice.dto.requestDTO.OrderExchangeReq;
import com.buyit.customerservice.dto.responseDTO.CartRes;
import com.buyit.customerservice.model.Cart;
import com.buyit.customerservice.model.CartItem;
import com.buyit.customerservice.repository.CartItemRepo;
import com.buyit.customerservice.repository.CartRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CartService {
    private final CartRepo cartRepository;

    private final CartItemRepo cartItemRepo;

    private final ProductClient productClient;

    public Cart addToCart(Cart cart, ProductRes product, int quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setProductId(product.getProductId());
        cartItem.setQuantity(quantity);
        cartItem.setSubtotal(product.getPrice()* quantity);
        cart.setTotalPrice(cart.getTotalPrice() + cartItem.getSubtotal());
        cartRepository.save(cart);
        cartItem.setCartId(cart.getId());
        cartItemRepo.save(cartItem);
        return cart;
    }

    public String refreshCart(long cartId) {
        List<CartItem> cartItems = cartItemRepo.findByCartId(cartId);
        String message = "Your cart is up to date"; // Set a default message

        for (CartItem c : cartItems) {
            long productQuantity = Long.parseLong(productClient.getProductById(c.getProductId()).getQuantity());
            if (c.getQuantity() > productQuantity) {
                cartItemRepo.deleteById(c.getId());
                message = "CartItem removed as the product is out of stock";
            }
        }

        return message; // Return the message
    }


    public CartRes getCartRes(Long id) {
        Cart cart = cartRepository.findById(id).get();
        List<CartItem> cartItems = cartItemRepo.findByCartId(id);
        List<CartItemDto> cartItemDtos = new ArrayList<>();

        for(CartItem c : cartItems){
            CartItemDto cd = new CartItemDto();
            cd.setQuantity(c.getQuantity());
            cd.setProductId(c.getProductId());
            cd.setSubtotal(c.getSubtotal());
            cartItemDtos.add(cd);
        }

        CartRes cartRes = new CartRes(cart.getId(),cart.getTotalPrice(),cartItemDtos);

        return cartRes;
    }

    public Cart getCart(long id){
        return cartRepository.findById(id).get();
    }


    public Double getCheckOutAmount(OrderExchangeReq oer) {
        List<CartItem> cartItems = cartItemRepo.findByCartId(oer.getCartId());
        Double amount = (double) 0;

        for(Long c:oer.getCartItemId()){
            for(CartItem x:cartItems){
                if(x.getId().equals(c)){
                    productClient.updateProductQuantity(x.getProductId(),x.getQuantity());
                    x.setOrderId(c);
                    amount += x.getSubtotal();
                    cartItemRepo.save(x);
                }
            }
        }
        return amount;
    }

    public void deleteCartItems(List<Long> cartItemIds, long cartId) {
        List<CartItem> cartItems = cartItemRepo.findByCartId(cartId);
        Cart cart = cartRepository.findById(cartId).orElse(null);

        if (cart != null) {
            for (Long itemId : cartItemIds) {
                // Find and delete the cart item with the specified itemId
                cartItems.stream()
                        .filter(cartItem -> cartItem.getId().equals(itemId))
                        .findFirst()
                        .ifPresent(cartItem -> {
                            // Deduct the subtotal from the cart total
                            cart.setTotalPrice(cart.getTotalPrice() - cartItem.getSubtotal());
                            cartRepository.save(cart);

                            // Delete the cart item
                            cartItemRepo.delete(cartItem);
                        });
            }
        }
    }



}
