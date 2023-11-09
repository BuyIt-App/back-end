package com.buyit.productservice.controller;

public class InventoryKeeperNotFoundException extends RuntimeException {

    public InventoryKeeperNotFoundException(String message) {
        super(message);
    }

    public InventoryKeeperNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
