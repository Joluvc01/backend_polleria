package com.api_polleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductStoreStockDTO {

    private Long id;
    private Long product;
    private Long store;
    private int quantity;
}