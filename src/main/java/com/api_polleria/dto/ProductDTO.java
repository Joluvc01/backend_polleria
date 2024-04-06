package com.api_polleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class ProductDTO implements Serializable {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private Boolean status;
    private Double valoration;
    private List<String> categoryList;
}
