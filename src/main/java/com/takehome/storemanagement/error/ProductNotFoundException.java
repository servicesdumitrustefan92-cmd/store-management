package com.takehome.storemanagement.error;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String sku) {
        super("Product not found for sku=" + sku);
    }
}
