package com.niq.activate.service.impl;

import com.niq.activate.dto.PersonalizedProductResponseDTO;
import com.niq.activate.dto.ShopperProductListDTO;
import com.niq.activate.dto.ShopperShelfDTO;
import com.niq.activate.entity.Product;
import com.niq.activate.entity.Shopper;
import com.niq.activate.entity.ShopperProductKey;
import com.niq.activate.entity.ShopperProductView;
import com.niq.activate.entity.ShopperProducts;
import com.niq.activate.exception.ResourceNotFoundException;
import com.niq.activate.repository.ShopperProductRepository;
import com.niq.activate.repository.ShopperProductViewRepository;
import com.niq.activate.repository.ShopperRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShopperServiceImplTest {

    @Mock
    private ShopperRepository shopperRepository;

    @Mock
    private ShopperProductRepository shopperProductRepository;

    @Mock
    private ShopperProductViewRepository shopperProductViewRepository;

    @InjectMocks
    private ShopperServiceImpl shopperService;

    private ShopperProductListDTO shopperProductListDto;
    private Shopper shopper;
    private List<ShopperProducts> shopperProducts;
    private List<ShopperProductView> shopperProductViews;
    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .productId("123")
                .category("Test Category")
                .brand("Test Brand")
                .build();

        shopperProductListDto = ShopperProductListDTO.builder()
                .shopperId("shopper123")
                .shelf(Arrays.asList(
                        ShopperShelfDTO.builder()
                                .productId("123")
                                .relevancyScore(0.8)
                                .build()
                ))
                .build();

        shopper = Shopper.builder()
                .shopperId("shopper123")
                .build();

        ShopperProducts shopperProduct = ShopperProducts.builder()
                .shopperId("shopper123")
                .productId("123")
                .relevancyScore(0.8)
                .product(product)
                .build();

        ShopperProductKey shopperProductKey = ShopperProductKey.builder()
                .shopperId("shopper123")
                .productId("123")
                .build();
        ShopperProductView shopperProductView = ShopperProductView.builder()
                .shopperProductId(shopperProductKey)
                .category("Test Category")
                .brand("Test Brand")
                .relevancyScore(0.8)
                .build();
        shopperProductViews = List.of(shopperProductView);
        shopperProducts = Arrays.asList(shopperProduct);
    }

    @Test
    void saveShopperProducts_NewShopper_SavesSuccessfully() {
        when(shopperRepository.findById("shopper123")).thenReturn(Optional.empty());
        when(shopperRepository.save(any(Shopper.class))).thenReturn(shopper);
        when(shopperProductRepository.saveAll(any())).thenReturn(shopperProducts);

        shopperService.saveShopperProducts(shopperProductListDto);

        verify(shopperRepository).findById("shopper123");
        verify(shopperRepository).save(any(Shopper.class));
        verify(shopperProductRepository).deleteByShopperId("shopper123");
        verify(shopperProductRepository).saveAll(any());
    }

    @Test
    void saveShopperProducts_ExistingShopper_UpdatesSuccessfully() {
        when(shopperRepository.findById("shopper123")).thenReturn(Optional.of(shopper));
        when(shopperProductRepository.saveAll(any())).thenReturn(shopperProducts);

        shopperService.saveShopperProducts(shopperProductListDto);

        verify(shopperRepository).findById("shopper123");
        verify(shopperRepository, never()).save(any(Shopper.class));
        verify(shopperProductRepository).deleteByShopperId("shopper123");
        verify(shopperProductRepository).saveAll(any());
    }

    @Test
    void getShopperProducts_ValidShopper_ReturnsProducts() {
        when(shopperRepository.findById("shopper123")).thenReturn(Optional.of(shopper));
        when(shopperProductViewRepository.getShopperProductPersonalized(
                eq("shopper123"), eq("Test Category"), eq("Test Brand"), eq(10)))
                .thenReturn(shopperProductViews);

        List<PersonalizedProductResponseDTO> result = shopperService.getShopperProducts(
                "shopper123", "Test Category", "Test Brand", 10);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductId()).isEqualTo("123");
        verify(shopperProductViewRepository).getShopperProductPersonalized(
                eq("shopper123"), eq("Test Category"), eq("Test Brand"), eq(10));
    }

    @Test
    void getShopperProducts_NonExistentShopper_ThrowsException() {
        when(shopperRepository.findById("nonexistent"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                shopperService.getShopperProducts("nonexistent", null, null, 10));

        verify(shopperProductViewRepository, never())
                .getShopperProductPersonalized(any(), any(), any(), any());
    }

    @Test
    void getShopperProducts_NoFilters_ReturnsAllProducts() {
        when(shopperRepository.findById("shopper123")).thenReturn(Optional.of(shopper));
        when(shopperProductViewRepository.getShopperProductPersonalized(
                eq("shopper123"), isNull(), isNull(), any()))
                .thenReturn(shopperProductViews);

        List<PersonalizedProductResponseDTO> result = shopperService.getShopperProducts(
                "shopper123", null, null, 10);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        verify(shopperProductViewRepository).getShopperProductPersonalized(
                eq("shopper123"), isNull(), isNull(), 
                eq(10));
    }
} 