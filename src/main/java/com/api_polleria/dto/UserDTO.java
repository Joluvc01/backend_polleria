package com.api_polleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UserDTO implements Serializable {

    private Long id;
    private String username;
    private String fullname;
    private Boolean status;
}
