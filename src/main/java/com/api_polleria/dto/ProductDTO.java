package com.api_polleria.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ProductDTO implements Serializable {

    private UUID id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private Boolean status;
    private List<String> categoryList;
}
