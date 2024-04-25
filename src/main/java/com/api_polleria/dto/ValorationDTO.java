package com.api_polleria.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValorationDTO implements Serializable {

    private Long id;
    private Integer valoration;
    private String review;
    private Long productId;
    private Long customerId;
    private String customerName;
    private String customerLastname;
    private LocalDate date;
}