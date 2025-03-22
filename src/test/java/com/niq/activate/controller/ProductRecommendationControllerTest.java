package com.niq.activate.controller;

import com.niq.activate.dto.PersonalizedProductResponseDTO;
import com.niq.activate.service.impl.ShopperServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductRecommendationController.class)
class ProductRecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShopperServiceImpl shopperService;

    private List<PersonalizedProductResponseDTO> mockProducts;

    @BeforeEach
    void setUp() {
        mockProducts = Arrays.asList(
                PersonalizedProductResponseDTO.builder()
                        .productId("123")
                        .category("Category 1")
                        .brand("Brand 1")
                        .relevancyScore(0.9)
                        .build(),
                PersonalizedProductResponseDTO.builder()
                        .productId("456")
                        .category("Category 2")
                        .brand("Brand 2")
                        .relevancyScore(0.8)
                        .build()
        );
    }

    @Test
    @WithMockUser(roles = "EXTERNAL")
    void getPersonalizedProducts_ValidInput_ReturnsProducts() throws Exception {
        when(shopperService.getShopperProducts(anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(mockProducts);

        mockMvc.perform(get("/api/external/products")
                        .param("shopperId", "shopper123")
                        .param("category", "Category 1")
                        .param("brand", "Brand 1")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].productId").value("123"));
    }

    @Test
    void getPersonalizedProducts_Unauthorized_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/external/products")
                        .param("shopperId", "shopper123"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "WRONG_ROLE")
    void getPersonalizedProducts_WrongRole_ReturnsOK() throws Exception {
        mockMvc.perform(get("/api/external/products")
                        .param("shopperId", "shopper123"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "EXTERNAL")
    void getPersonalizedProducts_NoShopperId_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/external/products"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "EXTERNAL")
    void getPersonalizedProducts_NoResults_ReturnsEmptyList() throws Exception {
        when(shopperService.getShopperProducts(anyString(), anyString(), anyString(), anyInt()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/external/products")
                        .param("shopperId", "nonexistent")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser(roles = "EXTERNAL")
    void getPersonalizedProducts_DefaultLimit_ReturnsProducts() throws Exception {
        when(shopperService.getShopperProducts(anyString(), isNull(), isNull(), eq(10)))
                .thenReturn(mockProducts);

        mockMvc.perform(get("/api/external/products")
                        .param("shopperId", "shopper123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(roles = "INTERNAL")
    void getPersonalizedProducts_WithFilters_ReturnsFilteredProducts() throws Exception {
        List<PersonalizedProductResponseDTO> filteredProducts = Collections.singletonList(mockProducts.get(0));
        when(shopperService.getShopperProducts(eq("shopper123"), eq("Category 1"), eq("Brand 1"), anyInt()))
                .thenReturn(filteredProducts);

        mockMvc.perform(get("/api/external/products")
                        .param("shopperId", "shopper123")
                        .param("category", "Category 1")
                        .param("brand", "Brand 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].category").value("Category 1"))
                .andExpect(jsonPath("$[0].brand").value("Brand 1"));
    }
} 