package com.niq.activate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PersonalizedProductResponseDTO {
    private String productId;
    private String category;
    private String brand;
    private Double relevancyScore;
}
