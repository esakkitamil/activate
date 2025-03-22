package com.niq.activate.service;

import com.niq.activate.dto.ProductMetadataDTO;
import com.niq.activate.entity.Product;

public interface ProductService {

    Product saveProduct(ProductMetadataDTO productMetadataDto);
    Product getProduct(String productId);
    void updateProduct(ProductMetadataDTO productMetadataDto);

    }
