package com.api_polleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class RegisterCustomerDTO {

    private String name;
    private String lastname;
    private String email;
    private String password;
    private LocalDate birthdate;
    private Boolean status;
}
