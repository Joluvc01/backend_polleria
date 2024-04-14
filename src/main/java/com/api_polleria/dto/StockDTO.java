package com.api_polleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockDTO {

    private String store;
    private int quantity;

}
