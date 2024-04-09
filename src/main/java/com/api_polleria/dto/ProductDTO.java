package com.api_polleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class ProductDTO implements Serializable {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String cardImage;
    private String detailImage;
    private Set<String> galleryImages;
    private Boolean status;
    private Double valoration;
    private List<String> categoryList;
}
