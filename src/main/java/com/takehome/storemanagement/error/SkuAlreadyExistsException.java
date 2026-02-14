package com.takehome.storemanagement.error;

public class SkuAlreadyExistsException extends RuntimeException {
    public SkuAlreadyExistsException(String sku) {
        super("Product already exists for sku=" + sku);
    }
}
