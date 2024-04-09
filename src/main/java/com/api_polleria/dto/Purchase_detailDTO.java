package com.api_polleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Purchase_detailDTO {

        private Long product;
        private Integer quantity;
        private Double total;
}
