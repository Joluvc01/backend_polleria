package com.api_polleria.controller;

import com.api_polleria.entity.Category;
import com.api_polleria.service.CategoryService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(required = false) Boolean status,
                                     Pageable pageable) {

        Specification<Category> spec = (root, query, cb) -> {
            if (status == null) {
                return cb.isTrue(cb.literal(true));
            } else {
                return cb.equal(root.get("status"), status);
            }
        };

        Page<Category> categoryPage = categoryService.findAll(spec, pageable);
        return ResponseEntity.ok(categoryPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return categoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Category category) {
        Category exist = categoryService.findByName(category.getName());
        if (exist != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La categoria ya existe");
        }
        category.setStatus(true);
        categoryService.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body("Categoria Creada");

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Category category) {
        Optional<Category> optionalCategory = categoryService.findById(id);
        if (optionalCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La categoria no existe");
        }
        category.setId(id);
        categoryService.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body("Categoria Actualizada");

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        Optional<Category> optionalCategory = categoryService.findById(id);
        if (optionalCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La categoria no existe");
        }
        categoryService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Categoria Eliminada");

    }
}