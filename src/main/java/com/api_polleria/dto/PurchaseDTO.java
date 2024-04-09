package com.api_polleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class PurchaseDTO {

        private Long id;
        private LocalDate date;
        private List<Purchase_detailDTO> details;
        private Long customer;
        private Double igv;
        private Double subtotal;
        private Double total;
}
