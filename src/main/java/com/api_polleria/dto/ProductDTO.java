package com.api_polleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ProductDTO implements Serializable {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private Boolean status;
    private List<String> categoryList;
}
