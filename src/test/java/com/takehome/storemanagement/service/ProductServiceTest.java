package com.takehome.storemanagement.service;

import com.takehome.storemanagement.domain.Product;
import com.takehome.storemanagement.dto.PriceChangeRequest;
import com.takehome.storemanagement.dto.ProductCreateRequest;
import com.takehome.storemanagement.dto.ProductResponse;
import com.takehome.storemanagement.error.ProductNotFoundException;
import com.takehome.storemanagement.error.SkuAlreadyExistsException;
import com.takehome.storemanagement.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductRepository repository;
    private ProductService service;

    @BeforeEach
    void setUp() {
        repository = mock(ProductRepository.class);
        service = new ProductService(repository);
    }

    @Test
    void create_shouldSaveAndReturnResponse_whenSkuDoesNotExist() {
        ProductCreateRequest request = new ProductCreateRequest(
                "TV-55-LED-001",
                "Samsung 55 inch LED TV",
                new BigDecimal("2499.99"),
                "RON"
        );

        when(repository.existsBySku("TV-55-LED-001")).thenReturn(false);
        when(repository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponse response = service.create(request);

        assertThat(response.sku()).isEqualTo("TV-55-LED-001");
        assertThat(response.name()).isEqualTo("Samsung 55 inch LED TV");
        assertThat(response.price()).isEqualByComparingTo("2499.99");
        assertThat(response.currency()).isEqualTo("RON");

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(repository).save(captor.capture());
        Product saved = captor.getValue();

        assertThat(saved.getSku()).isEqualTo("TV-55-LED-001");
        assertThat(saved.getName()).isEqualTo("Samsung 55 inch LED TV");
        assertThat(saved.getPrice()).isEqualByComparingTo("2499.99");
        assertThat(saved.getCurrency()).isEqualTo("RON");
    }

    @Test
    void create_shouldThrow_whenSkuAlreadyExists() {
        ProductCreateRequest request = new ProductCreateRequest(
                "SKU-123",
                "Milk 1L",
                new BigDecimal("12.49"),
                "RON"
        );

        when(repository.existsBySku("SKU-123")).thenReturn(true);

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(SkuAlreadyExistsException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void findBySku_shouldReturnResponse_whenProductExists() {
        Product product = new Product("SKU-777", "Coffee", new BigDecimal("25.00"), "RON");
        when(repository.findBySku("SKU-777")).thenReturn(Optional.of(product));

        ProductResponse response = service.findBySku("SKU-777");

        assertThat(response.sku()).isEqualTo("SKU-777");
        assertThat(response.name()).isEqualTo("Coffee");
        assertThat(response.price()).isEqualByComparingTo("25.00");
        assertThat(response.currency()).isEqualTo("RON");
    }

    @Test
    void findBySku_shouldThrow_whenProductDoesNotExist() {
        when(repository.findBySku("MISSING")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findBySku("MISSING"))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("MISSING");

        verify(repository).findBySku("MISSING");
        verifyNoMoreInteractions(repository);
    }


    @Test
    void changePrice_shouldUpdatePrice_whenProductExists() {
        Product product = new Product("SKU-999", "Tea", new BigDecimal("10.00"), "RON");
        when(repository.findBySku("SKU-999")).thenReturn(Optional.of(product));

        PriceChangeRequest request = new PriceChangeRequest(new BigDecimal("12.50"));

        ProductResponse response = service.changePrice("SKU-999", request);

        assertThat(response.price()).isEqualByComparingTo("12.50");
        // also asserts the entity was updated in memory
        assertThat(product.getPrice()).isEqualByComparingTo("12.50");
    }

    @Test
    void changePrice_shouldThrow_whenProductDoesNotExist() {
        // Arrange
        when(repository.findBySku("MISSING")).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> service.changePrice("MISSING", new PriceChangeRequest(new BigDecimal("9.99"))))
                .isInstanceOf(ProductNotFoundException.class);
    }
}
