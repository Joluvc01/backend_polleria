package com.api_polleria.repository;

import com.api_polleria.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContaining(String contain);

    Product findByName(String name);

    List<Product> findByCategoryList_Name(String categoryName);

}
