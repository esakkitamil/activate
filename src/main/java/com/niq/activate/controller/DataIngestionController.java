package com.niq.activate.controller;

import com.niq.activate.dto.ProductMetadataDTO;
import com.niq.activate.dto.ShopperProductListDTO;
import com.niq.activate.service.ProductService;
import com.niq.activate.service.ShopperService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal")
@RequiredArgsConstructor
@Tag(name = "Data Ingestion", description = "Endpoints for ingesting data from the data team")
public class DataIngestionController {

    private final ProductService productService;
    private final ShopperService shopperService;

    @Operation(
            summary = "Save product metadata",
            description = "Saves or updates product metadata including category and brand"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product metadata saved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/product-metadata")
    public ResponseEntity<Void> saveProductMetadata(@RequestBody @Valid ProductMetadataDTO productMetadataDTO) {
        productService.saveProduct(productMetadataDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Save shopper's personalized shelf",
            description = "Saves a shopper's personalized product shelf with relevancy scores"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Shopper shelf saved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/shopper-shelf")
    public ResponseEntity<Void> saveShopperShelf(@RequestBody @Valid ShopperProductListDTO shopperProductListDto) {
        shopperService.saveShopperProducts(shopperProductListDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update product metadata",
            description = "Updates product metadata including category and brand"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product metadata updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/product-metadata")
    public ResponseEntity<Void> updateProductMetadata(@RequestBody @Valid ProductMetadataDTO productMetadataDTO) {
        productService.updateProduct(productMetadataDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
