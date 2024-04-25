package com.api_polleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CustomerDTO implements Serializable {

    private Long id;
    private String name;
    private String lastname;
    private String email;
    private LocalDate birthdate;
    private String status;
}
