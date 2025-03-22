package com.niq.activate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shopper_products",
        indexes = {
                @Index(name = "idx_shopper_id", columnList = "shopper_id"),
                @Index(name = "idx_product_id", columnList = "product_id")
        })@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(ShopperProductKey.class)  // Use composite key
public class ShopperProducts {

    @Id
    @Column(name = "shopper_id")
    private String shopperId;

    @Id
    @Column(name = "product_id")
    private String productId;

    @Column(name = "relevancy_score")
    private Double relevancyScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false)
    private Product product;
}