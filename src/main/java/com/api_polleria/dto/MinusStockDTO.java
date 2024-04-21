package com.api_polleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MinusStockDTO {

    private String product;
    private int quantity;
}
