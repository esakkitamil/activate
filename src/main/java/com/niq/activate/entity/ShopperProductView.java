package com.niq.activate.entity;


import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shopper_product_summary")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopperProductView {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride( name = "shopper_id", column = @Column(name = "shopper_id")),
            @AttributeOverride( name = "product_id", column = @Column(name = "product_id"))
    })
    private ShopperProductKey shopperProductId;
    @Column(name = "category")
    private String category;
    @Column(name = "brand")
    private String brand;
    @Column(name = "relevancy_score")
    private Double relevancyScore;
}
