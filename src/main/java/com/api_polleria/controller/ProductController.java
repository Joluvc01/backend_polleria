package com.api_polleria.controller;

import com.api_polleria.dto.ProductDTO;
import com.api_polleria.dto.StockDTO;
import com.api_polleria.entity.Category;
import com.api_polleria.entity.Product;
import com.api_polleria.entity.ProductStoreStock;
import com.api_polleria.service.*;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProductStoreStockService productStoreStockService;
    private final ConvertDTO convertDTO;

    public ProductController(ProductService productService, CategoryService categoryService, ProductStoreStockService productStoreStockService, ConvertDTO convertDTO) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.productStoreStockService = productStoreStockService;
        this.convertDTO = convertDTO;
    }

    private List<Category> getCategoryListFromNames(Set<String> categoryNames) {
        List<Category> categories = new ArrayList<>();
        for (String categoryName : categoryNames) {
            Category category = categoryService.findByName(categoryName);
            if (category == null) {
                return null;
            }
            categories.add(category);
        }
        return categories;
    }

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String partialProduct,
            Pageable pageable) {

        Specification<Product> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (category != null) {
                predicates.add(cb.isMember(category, root.get("categoryList")));
            }

            if (partialProduct != null) {
                predicates.add(cb.like(root.get("name"), "%" + partialProduct + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<Product> productPage = productService.findAll(spec, pageable);
        Page<ProductDTO> dtoPage = productPage.map(convertDTO::convertToProductDTO);
        return ResponseEntity.ok(dtoPage);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        Optional<Product> product = productService.findById(id);
        if (product.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto no existe");
        }
        Product prod = product.get();
        List<ProductStoreStock> storeStocks = productStoreStockService.findByProduct(prod);
        List<StockDTO> stock = storeStocks
                .stream()
                .map(storeStock -> new StockDTO(
                        storeStock.getStore().getName(),
                        storeStock.getQuantity()
                ))
                .toList();
        ProductDTO productDTO = convertDTO.convertToProductDTO(prod);
        productDTO.setStock(stock);
        return ResponseEntity.status(HttpStatus.OK).body(productDTO);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductDTO productDTO) {
        Product exist = productService.findByName(productDTO.getName());
        if(exist != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El producto ya existe");
        }
        Set<String> uniqueCategoryNames = new HashSet<>(productDTO.getCategoryList());
        List<Category> categories = getCategoryListFromNames(uniqueCategoryNames);
        if (categories == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Al menos una de las categorías no existe");
        }

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCardImage(productDTO.getCardImage());
        product.setDetailImage(productDTO.getDetailImage());
        product.setGalleryImages(productDTO.getGalleryImages());
        product.setStatus(true);
        product.setCategoryList(categories);
        productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body("Producto Creado");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductDTO productDTO){
        Optional<Product> optionalProduct = productService.findById(id);
        if (optionalProduct.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto no existe");
        }
        Set<String> uniqueCategoryNames = new HashSet<>(productDTO.getCategoryList());
        List<Category> categories = getCategoryListFromNames(uniqueCategoryNames);
        if (categories == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Al menos una de las categorías no existe");
        }

        Product product = optionalProduct.get();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setCardImage(productDTO.getCardImage());
        product.setDetailImage(productDTO.getDetailImage());
        product.setGalleryImages(productDTO.getGalleryImages());
        product.setStatus(productDTO.getStatus());
        product.setCategoryList(categories);
        productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body("Producto Actualizado");
    }



}
