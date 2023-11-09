package com.buyit.customerservice.service;

import com.buyit.customerservice.client.ProductClient;
import com.buyit.customerservice.dto.CartItemDto;
import com.buyit.customerservice.dto.ProductRes;
import com.buyit.customerservice.dto.requestDTO.OrderExchangeReq;
import com.buyit.customerservice.dto.responseDTO.CartRes;
import com.buyit.customerservice.model.Cart;
import com.buyit.customerservice.model.CartItem;
import com.buyit.customerservice.dto.Product;
import com.buyit.customerservice.repository.CartItemRepo;
import com.buyit.customerservice.repository.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepo cartRepository;

    @Autowired
    private CartItemRepo cartItemRepo;

    @Autowired
    private ProductClient productClient;

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



}
