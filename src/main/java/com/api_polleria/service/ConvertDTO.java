package com.api_polleria.service;

import com.api_polleria.dto.ProductDTO;
import com.api_polleria.entity.Category;
import com.api_polleria.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConvertDTO {

    @Autowired
    private ProductService productService;

    public ProductDTO convertToProductDTO(Product product){
        List<String> categoryList = product.getCategoryList()
                .stream()
                .map(Category::getName)
                .toList();

        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.getStatus(),
                categoryList
        );
    }
}
