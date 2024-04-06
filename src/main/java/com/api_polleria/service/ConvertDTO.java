package com.api_polleria.service;

import com.api_polleria.dto.ProductDTO;
import com.api_polleria.dto.ProductStoreStockDTO;
import com.api_polleria.entity.Category;
import com.api_polleria.entity.Product;
import com.api_polleria.entity.ProductStoreStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConvertDTO {

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
                product.getValoration(),
                categoryList
        );
    }

    public ProductStoreStockDTO convertToProductStoreStockDTO(ProductStoreStock storeStock){
        Long product = (storeStock.getProduct() != null) ? storeStock.getProduct().getId() : null;
        Long store = (storeStock.getStore() != null) ? storeStock.getStore().getId() : null;

        return new ProductStoreStockDTO(
                storeStock.getId(),
                product,
                store,
                storeStock.getQuantity()
        );
    }
}
