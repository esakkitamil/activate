package com.niq.activate.repository;

import com.niq.activate.entity.ShopperProductKey;
import com.niq.activate.entity.ShopperProductView;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShopperProductViewRepository extends JpaRepository<ShopperProductView, ShopperProductKey> {
    @Query(
            value = "select * from shopper_product_summary WHERE shopper_id = :shopperId "+
                    " AND (:category IS NULL OR category = :category) " +
                    " AND (:brand IS NULL OR brand = :brand) " +
                    " ORDER BY relevancy_score DESC limit :limit",
            nativeQuery = true
    )
    List<ShopperProductView> getShopperProductPersonalized(@Param("shopperId") String shopperId,
                                                           @Param("category") String category,
                                                           @Param("brand") String brand,
                                                           @Param("limit") Integer limit);


    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(
            value = "refresh materialized view shopper_product_summary;",
            nativeQuery = true
    )
    void refreshShopperProductSummary();
}
