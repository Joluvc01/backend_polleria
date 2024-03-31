package com.api_polleria.service;

import com.api_polleria.entity.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryService {
    public List<Category> findAll();

    public Category findByName(String name);

    public Optional<Category> findById(UUID id);

    public Category save(Category category);

    public void deleteById(UUID id);
}