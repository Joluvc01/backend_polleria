package com.api_polleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MinusStockDTO implements Serializable {

    private String product;
    private int quantity;
}
