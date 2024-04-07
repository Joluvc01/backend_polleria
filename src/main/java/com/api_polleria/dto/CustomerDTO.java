package com.api_polleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class CustomerDTO {

    private Long id;
    private String name;
    private String lastname;
    private String email;
    private LocalDate birthdate;
    private Boolean status;
    private List<Long> addressList;
    private List<Long> favoriteProducts;
}
