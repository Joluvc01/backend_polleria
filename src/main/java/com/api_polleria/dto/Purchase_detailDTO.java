package com.api_polleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Purchase_detailDTO implements Serializable {

        private Long product;
        private Integer quantity;
        private Double total;
}
