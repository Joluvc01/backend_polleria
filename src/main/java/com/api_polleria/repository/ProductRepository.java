package com.api_polleria.repository;

import com.api_polleria.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Product findByName(String name);
    List<Product> findByCategoryList_Name(String categoryName);

}