package com.niq.activate.service;

import com.niq.activate.dto.PersonalizedProductResponseDTO;
import com.niq.activate.dto.ShopperProductListDTO;

import java.util.List;

public interface ShopperService {

    void saveShopperProducts(ShopperProductListDTO shopperProductListDto);
    List<PersonalizedProductResponseDTO> getShopperProducts(String shopperId, String category, String brand, Integer limit);
}
