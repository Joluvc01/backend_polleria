package com.api_polleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class StockDTO implements Serializable {

    private String store;
    private int quantity;

}
