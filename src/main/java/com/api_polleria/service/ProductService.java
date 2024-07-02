package com.api_polleria.service;

import com.api_polleria.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Page<Product> findAll(Pageable pageable);

    Optional<Product> findById(Long id);

    Product findByName(String name);

    Product save(Product product);

    void deleteById(Long id);
}
