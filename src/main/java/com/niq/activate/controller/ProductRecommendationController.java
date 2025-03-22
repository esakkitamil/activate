package com.niq.activate.controller;

import com.niq.activate.dto.PersonalizedProductResponseDTO;
import com.niq.activate.service.ShopperService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/external")
@RequiredArgsConstructor
@Tag(name = "Product Recommendations", description = "Endpoints for retrieving personalized product recommendations")
public class ProductRecommendationController {

    private final ShopperService shopperService;

    @Operation(
            summary = "Get personalized product recommendations",
            description = "Retrieves personalized product recommendations for a shopper with optional filtering by category and brand"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved product recommendations"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
            @ApiResponse(responseCode = "404", description = "Shopper not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/products")
    public ResponseEntity<List<PersonalizedProductResponseDTO>> getPersonalizedProducts(
            @Parameter(description = "ID of the shopper", required = true)
            @RequestParam("shopperId") String shopperId,

            @Parameter(description = "Filter by product category")
            @RequestParam(value = "category", required = false) String category,

            @Parameter(description = "Filter by product brand")
            @RequestParam(value = "brand", required = false) String brand,

            @Parameter(description = "Maximum number of products to return (default: 10, max: 100)")
            @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit) {

        List<PersonalizedProductResponseDTO> personalizedProductResponseDTOS = shopperService.getShopperProducts(shopperId, category, brand, limit);
        return ResponseEntity.ok(personalizedProductResponseDTOS);
    }
}