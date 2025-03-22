package com.niq.activate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductMetadataDTO {
    @NotBlank(message = "Product Id Cannot be Empty")
    private String productId;
    @NotBlank(message = "Product Id Cannot be Empty")
    private String category;
    @NotBlank(message = "brand Id Cannot be Empty")
    private String brand;
}
