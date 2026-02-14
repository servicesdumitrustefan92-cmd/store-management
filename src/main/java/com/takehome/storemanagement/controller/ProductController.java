package com.takehome.storemanagement.controller;

import com.takehome.storemanagement.dto.PriceChangeRequest;
import com.takehome.storemanagement.dto.ProductCreateRequest;
import com.takehome.storemanagement.dto.ProductResponse;
import com.takehome.storemanagement.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prodducts")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody ProductCreateRequest request) {
        return service.create(request);
    }

    @GetMapping("/{sku}")
    public ProductResponse findBySku(@PathVariable String sku) {
        return service.findBySku(sku);
    }

    @PatchMapping("/{sku}/price")
    public ProductResponse changePrice(@PathVariable String sku,
                                       @Valid @RequestBody PriceChangeRequest request) {
        return service.changePrice(sku, request);
    }
}
