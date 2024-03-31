package com.api_polleria.service;

import com.api_polleria.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {
    public List<Product> findAll();

    public Optional<Product> findById(UUID id);

    Product findByName(String name);

    List<Product> findByCategoryName(String category);

    public Product save(Product product);

    public void deleteById(UUID id);
}
