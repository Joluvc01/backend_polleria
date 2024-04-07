package com.api_polleria.service;

import com.api_polleria.dto.CustomerDTO;
import com.api_polleria.dto.ProductDTO;
import com.api_polleria.dto.ProductStoreStockDTO;
import com.api_polleria.entity.*;
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

    public CustomerDTO convertToCustomerDTO(Customer customer){
        List<Long> addressList = customer.getAddressList()
                .stream()
                .map(Address::getId)
                .toList();
        List<Long> favoriteProducts = customer.getFavoriteProducts()
                .stream()
                .map(Product::getId)
                .toList();

        return new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getLastname(),
                customer.getEmail(),
                customer.getBirthdate(),
                customer.getStatus(),
                addressList,
                favoriteProducts
        );
    }
}
