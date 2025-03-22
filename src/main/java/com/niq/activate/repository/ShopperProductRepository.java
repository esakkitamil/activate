package com.niq.activate.repository;

import com.niq.activate.entity.ShopperProducts;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopperProductRepository extends JpaRepository<ShopperProducts, Long> {

    //
//    @Query("SELECT sp FROM ShopperProducts sp JOIN FETCH sp.product p " +
//            "WHERE sp.shopperId = :shopperId " +
//            "AND (:category IS NULL OR p.category = :category) " +
//            "AND (:brand IS NULL OR p.brand = :brand) " +
//            "ORDER BY sp.relevancyScore DESC")
//    List<ShopperProducts> findByShopperIdWithFilters(@Param("shopperId") String shopperId, @Param("category") String category,
//            @Param("brand") String brand,
//            Pageable pageable);

    void deleteByShopperId(String shopperId);

}
