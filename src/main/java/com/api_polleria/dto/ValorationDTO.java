package com.api_polleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValorationDTO {

    private Long id;
    private Integer valoration;
    private String review;
    private Long productId;
    private Long customerId;
}