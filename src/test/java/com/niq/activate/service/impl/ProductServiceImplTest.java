package com.niq.activate.service.impl;

import com.niq.activate.dto.ProductMetadataDTO;
import com.niq.activate.entity.Product;
import com.niq.activate.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductMetadataDTO productMetadataDto;
    private Product product;

    @BeforeEach
    void setUp() {
        productMetadataDto = ProductMetadataDTO.builder()
                .productId("123")
                .category("Test Category")
                .brand("Test Brand")
                .build();

        product = Product.builder()
                .productId("123")
                .category("Test Category")
                .brand("Test Brand")
                .build();
    }

    @Test
    void saveProduct_ValidProduct_ReturnsSavedProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.saveProduct(productMetadataDto);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getProductId()).isEqualTo(productMetadataDto.getProductId());
        assertThat(savedProduct.getCategory()).isEqualTo(productMetadataDto.getCategory());
        assertThat(savedProduct.getBrand()).isEqualTo(productMetadataDto.getBrand());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getProduct_ExistingProduct_ReturnsProduct() {
        when(productRepository.findById("123")).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProduct("123");

        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getProductId()).isEqualTo("123");
        verify(productRepository, times(1)).findById("123");
    }

    @Test
    void getProduct_NonExistingProduct_ReturnsNull() {
        when(productRepository.findById("nonexistent")).thenReturn(Optional.empty());

        Product foundProduct = productService.getProduct("nonexistent");

        assertThat(foundProduct).isNull();
        verify(productRepository, times(1)).findById("nonexistent");
    }
} 