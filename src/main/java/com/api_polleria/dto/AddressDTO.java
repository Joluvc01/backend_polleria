package com.api_polleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddressDTO {

    private Long id;
    private String address;
    private String district;
    private String province;
    private String state;
}