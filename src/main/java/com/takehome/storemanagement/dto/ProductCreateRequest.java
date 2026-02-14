package com.takehome.storemanagement.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductCreateRequest(
        @NotBlank
        @Pattern(regexp = "^[A-Z0-9-]{3,40}$", message = "SKU must contain 3-40 alphanumeric characters or hyphens")
        String sku,

        @NotBlank
        @Size(min = 2, max = 120)
        String name,

        @NotNull
        @Positive
        BigDecimal price,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter ISO code")
        String currency
) {}
