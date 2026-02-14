package com.takehome.storemanagement.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record PriceChangeRequest(
        @NotNull
        @Positive
        BigDecimal newPrice
) {}
