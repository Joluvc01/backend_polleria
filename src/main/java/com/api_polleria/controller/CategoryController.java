package com.api_polleria.controller;

import com.api_polleria.entity.Category;
import com.api_polleria.service.CategoryService;
import com.api_polleria.service.ConvertDTO;
import com.api_polleria.service.UtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final UtilsService utilsService;

    public CategoryController(CategoryService categoryService, UtilsService utilsService) {
        this.categoryService = categoryService;
        this.utilsService = utilsService;
    }

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(required = false) Boolean status,
            Pageable pageable) {

        List<Category> categoryList = categoryService.findAll(pageable).getContent();

        if (status != null) {
            categoryList = categoryList.stream()
                    .filter(category -> category.getStatus().equals(status))
                    .toList();
        }

        return utilsService.createPageResponse(categoryList, pageable, Function.identity());
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