
// ShopperProductListDto.java
package com.niq.activate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopperProductListDTO {
    @NotBlank
    private String shopperId;

    @NotEmpty
    private List<ShopperShelfDTO> shelf;
}