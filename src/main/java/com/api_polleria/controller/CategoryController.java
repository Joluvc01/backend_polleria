package com.api_polleria.controller;

import com.api_polleria.entity.Category;
import com.api_polleria.repository.CategoryRepository;
import com.api_polleria.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<?> findAll(Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id){
        return categoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Category category){
        Category exist = categoryService.findByName(category.getName());
        if(exist != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La categoria ya existe");
        } else {
            category.setStatus(true);
            categoryService.save(category);
            return ResponseEntity.status(HttpStatus.CREATED).body("Categoria Creada");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Category category){
        Optional<Category> optionalCategory = categoryService.findById(id);
        if(optionalCategory.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La categoria no existe");
        } else {
                category.setId(id);
                categoryService.save(category);
                return ResponseEntity.status(HttpStatus.CREATED).body("Categoria Actualizada");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id){
        Optional<Category> optionalCategory = categoryService.findById(id);
        if(optionalCategory.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La categoria no existe");
        } else {
            categoryService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Categoria Eliminada");
        }
    }
}