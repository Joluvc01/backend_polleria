package com.api_polleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class PurchaseDTO implements Serializable {

        private Long id;
        private LocalDate date;
        private List<Purchase_detailDTO> details;
        private Long customer;
        private Double igv;
        private Double subtotal;
        private Double total;
        private Boolean status;
        private Long store;
}
