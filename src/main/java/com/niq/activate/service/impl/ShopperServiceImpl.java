package com.niq.activate.service.impl;

import com.niq.activate.dto.PersonalizedProductResponseDTO;
import com.niq.activate.dto.ShopperProductListDTO;
import com.niq.activate.entity.Shopper;
import com.niq.activate.entity.ShopperProductView;
import com.niq.activate.entity.ShopperProducts;
import com.niq.activate.exception.ResourceNotFoundException;
import com.niq.activate.repository.ShopperProductRepository;
import com.niq.activate.repository.ShopperProductViewRepository;
import com.niq.activate.repository.ShopperRepository;
import com.niq.activate.service.ShopperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ShopperServiceImpl implements ShopperService {

    private final ShopperRepository shopperRepository;
    private final ShopperProductRepository shopperProductRepository;
    private final ShopperProductViewRepository shopperProductViewRepository;

    public ShopperServiceImpl(ShopperRepository shopperRepository, ShopperProductRepository shopperProductRepository, ShopperProductViewRepository shopperProductViewRepository) {
        this.shopperRepository = shopperRepository;
        this.shopperProductRepository = shopperProductRepository;
        this.shopperProductViewRepository = shopperProductViewRepository;
    }

    @Override
    @Transactional
    @CacheEvict(value = "shopperProducts", allEntries = true)
    public void saveShopperProducts(ShopperProductListDTO shopperProductListDto) {
        log.info("Saving product list for shopper: {}", shopperProductListDto.getShopperId());

        String shopperId = shopperProductListDto.getShopperId();
        shopperRepository.findById(shopperId)
                .orElseGet(() -> shopperRepository.save(Shopper.builder()
                        .shopperId(shopperId)
                        .build()));

        shopperProductRepository.deleteByShopperId(shopperId);

        List<ShopperProducts> shopperProducts = shopperProductListDto.getShelf().stream()
                .map(item -> ShopperProducts.builder()
                        .shopperId(shopperId)
                        .productId(item.getProductId())
                        .relevancyScore(item.getRelevancyScore())
                        .build())
                .toList();

        shopperProductRepository.saveAll(shopperProducts);
        shopperProductViewRepository.refreshShopperProductSummary();
    }

    @Override
    @Cacheable(value = "shopperProducts", key = "#shopperId + '-' + #category + '-' + #brand + '-' + #limit")
    @Transactional(readOnly = true)
    public List<PersonalizedProductResponseDTO> getShopperProducts(String shopperId, String category, String brand, Integer limit) {
        log.info("Fetching products for shopper: {} with filters category: {}, brand: {}, limit: {}",
                shopperId, category, brand, limit);

        shopperRepository.findById(shopperId)
                .orElseThrow(() -> new ResourceNotFoundException("Shopper not found with id: " + shopperId));

        List<ShopperProductView> list = shopperProductViewRepository.getShopperProductPersonalized(shopperId, category, brand, limit);

        return list.stream()
                .map(sp -> {
                    return PersonalizedProductResponseDTO.builder()
                            .productId(sp.getShopperProductId().getProductId())
                            .category(sp.getCategory())
                            .brand(sp.getBrand())
                            .relevancyScore(sp.getRelevancyScore())
                            .build();
                })
                .collect(Collectors.toList());
    }
}