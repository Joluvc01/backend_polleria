package com.api_polleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ProductStoreStockDTO implements Serializable {

    private Long product;
    private Long store;
    private int quantity;
}