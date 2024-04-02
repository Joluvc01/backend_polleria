package com.api_polleria.service;

import com.api_polleria.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    public Page<Product> findAll(Pageable pageable);

    public Optional<Product> findById(Long id);

    Product findByName(String name);

    List<Product> findByNameContaining(String contain);

    List<Product> findByCategoryList_Name(String category);

    public Product save(Product product);

    public void deleteById(Long id);
}
