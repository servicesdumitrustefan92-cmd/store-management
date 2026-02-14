package com.takehome.storemanagement.service;

import com.takehome.storemanagement.domain.Product;
import com.takehome.storemanagement.dto.PriceChangeRequest;
import com.takehome.storemanagement.dto.ProductCreateRequest;
import com.takehome.storemanagement.dto.ProductResponse;
import com.takehome.storemanagement.error.ProductNotFoundException;
import com.takehome.storemanagement.error.SkuAlreadyExistsException;
import com.takehome.storemanagement.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ProductResponse create(ProductCreateRequest request) {
        String sku = request.sku().trim();
        if (repository.existsBySku(sku)) {
            log.warn("Create product failed - sku already exists: {}", sku);
            throw new SkuAlreadyExistsException(sku);
        }

        Product product = new Product(
                sku,
                request.name().trim(),
                request.price(),
                request.currency().trim()
        );

        Product saved = repository.save(product);
        log.info("Product created: sku={}", saved.getSku());

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ProductResponse findBySku(String sku) {
        Product product = repository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException(sku));

        return toResponse(product);
    }

    @Transactional
    public ProductResponse changePrice(String sku, PriceChangeRequest request) {
        Product product = repository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException(sku));

        product.changePrice(request.newPrice());
        log.info("Price changed: sku={} newPrice={}", sku, request.newPrice());

        return toResponse(product);
    }

    private static ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getSku(),
                product.getName(),
                product.getPrice(),
                product.getCurrency()
        );
    }
}
