package com.api_polleria.controller;


import com.api_polleria.dto.ProductDTO;
import com.api_polleria.entity.Category;
import com.api_polleria.entity.Product;
import com.api_polleria.service.CategoryService;
import com.api_polleria.service.ConvertDTO;
import com.api_polleria.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ConvertDTO convertDTO;

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
        Page<Product> productPage = productService.findAll(pageable);
        Page<ProductDTO> productDTOPage = productPage.map(convertDTO::convertToProductDTO);
        return ResponseEntity.status(HttpStatus.OK).body(productDTOPage);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id){
        return productService.findById(id)
                .map(product -> ResponseEntity.ok(convertDTO.convertToProductDTO(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductDTO productDTO) {
        Product exist = productService.findByName(productDTO.getName());
        if(exist != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El producto ya existe");
        }
        Set<String> uniqueCategoryNames = new HashSet<>(productDTO.getCategoryList());
        List<Category> categories = new ArrayList<>();

        for (String categoryName : uniqueCategoryNames) {
            Category category = categoryService.findByName(categoryName);
            if (category == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La categoría '" + categoryName + "' no existe");
            }
            categories.add(category);
        }

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setImageUrl(productDTO.getImageUrl());
        product.setStatus(true);
        product.setCategoryList(categories);
        productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body("Producto Creado");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody ProductDTO productDTO){
        Optional<Product> optionalProduct = productService.findById(id);
        if (optionalProduct.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto no existe");
        }
        Set<String> uniqueCategoryNames = new HashSet<>(productDTO.getCategoryList());
        List<Category> categories = new ArrayList<>();

        for (String categoryName : uniqueCategoryNames) {
            Category category = categoryService.findByName(categoryName);
            if (category == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La categoría '" + categoryName + "' no existe");
            }
            categories.add(category);
        }

        Product product = optionalProduct.get();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setImageUrl(productDTO.getImageUrl());
        product.setStatus(productDTO.getStatus());
        product.setCategoryList(categories);
        productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body("Producto Actualizado");

    }


}
