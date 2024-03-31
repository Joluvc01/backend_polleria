package com.api_polleria.repository;

import com.api_polleria.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID>{
    Category findByName(String name);

}