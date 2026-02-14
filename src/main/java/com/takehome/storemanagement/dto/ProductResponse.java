package com.takehome.storemanagement.dto;

import java.math.BigDecimal;

public record ProductResponse(
        String sku,
        String name,
        BigDecimal price,
        String currency
) {}
