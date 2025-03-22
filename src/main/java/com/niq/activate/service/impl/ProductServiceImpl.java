package com.niq.activate.service.impl;

import com.niq.activate.dto.ProductMetadataDTO;
import com.niq.activate.entity.Product;
import com.niq.activate.exception.InvalidInputException;
import com.niq.activate.exception.ResourceNotFoundException;
import com.niq.activate.repository.ProductRepository;
import com.niq.activate.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Transactional
    @Override
    public Product saveProduct(ProductMetadataDTO productMetadataDto) {
        log.info("Saving product metadata for product: {}", productMetadataDto.getProductId());
        String productId = productMetadataDto.getProductId();
        productRepository.findById(productId)
                .ifPresent(s -> {
                    throw new InvalidInputException("Product Exist: " + productId);
                });

        Product product = Product.builder()
                .productId(productMetadataDto.getProductId())
                .category(productMetadataDto.getCategory())
                .brand(productMetadataDto.getBrand())
                .build();

        return productRepository.save(product);
    }

    @Cacheable(value = "products", key = "#productId")
    public Product getProduct(String productId) {
        log.info("Fetching product with ID: {}", productId);
        return productRepository.findById(productId)
                .orElse(null);
    }

    @Transactional
    @Override
    public void updateProduct(ProductMetadataDTO productMetadataDto) {
        log.info("Update product metadata for product: {}", productMetadataDto.getProductId());
        String productId = productMetadataDto.getProductId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product Doesn't Exists :" +productId));


        product.setCategory(productMetadataDto.getCategory());
        product.setBrand(productMetadataDto.getBrand());

        productRepository.save(product);
    }
}