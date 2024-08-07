package com.api_polleria.service;

import com.api_polleria.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface CategoryService {

    public Page<Category> findAll(Specification<Category> spec, Pageable pageable);

    public Category findByName(String name);

    public Optional<Category> findById(Long id);

    public Category save(Category category);

    public void deleteById(Long id);
}