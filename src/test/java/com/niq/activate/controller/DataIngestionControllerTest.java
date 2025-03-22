package com.niq.activate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.niq.activate.dto.ProductMetadataDTO;
import com.niq.activate.dto.ShopperProductListDTO;
import com.niq.activate.dto.ShopperShelfDTO;
import com.niq.activate.service.impl.ProductServiceImpl;
import com.niq.activate.service.impl.ShopperServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DataIngestionController.class)
class DataIngestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductServiceImpl productService;

    @MockBean
    private ShopperServiceImpl shopperService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductMetadataDTO productMetadataDto;
    private ShopperProductListDTO shopperProductListDto;

    @BeforeEach
    void setUp() {
        productMetadataDto = ProductMetadataDTO.builder()
                .productId("123")
                .category("Test Category")
                .brand("Test Brand")
                .build();

        shopperProductListDto = ShopperProductListDTO.builder()
                .shopperId("shopper123")
                .shelf(Arrays.asList(
                        ShopperShelfDTO.builder()
                                .productId("123")
                                .relevancyScore(2.0)
                                .build()
                ))
                .build();
    }

    @Test
    @WithMockUser(roles = "INTERNAL")
    void saveProductMetadata_ValidInput_ReturnsCreated() throws Exception {
        when(productService.saveProduct(any(ProductMetadataDTO.class)))
                .thenReturn(null);

        mockMvc.perform(post("/api/internal/product-metadata")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productMetadataDto)))
                .andExpect(status().isCreated());

        verify(productService).saveProduct(any(ProductMetadataDTO.class));
    }

    @Test
    void saveProductMetadata_Unauthorized_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(post("/api/internal/product-metadata")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productMetadataDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "INTERNAL")
    void saveProductMetadata_InvalidInput_ReturnsBadRequest() throws Exception {
        ProductMetadataDTO invalidDto = ProductMetadataDTO.builder().build();

        mockMvc.perform(post("/api/internal/product-metadata")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "INTERNAL")
    void saveShopperShelf_ValidInput_ReturnsCreated() throws Exception {
        doNothing().when(shopperService).saveShopperProducts(any(ShopperProductListDTO.class));

        mockMvc.perform(post("/api/internal/shopper-shelf")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shopperProductListDto)))
                .andExpect(status().isCreated());

        verify(shopperService).saveShopperProducts(any(ShopperProductListDTO.class));
    }
} 