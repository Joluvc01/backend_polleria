package com.api_polleria.controller;

import com.api_polleria.dto.ProductDTO;
import com.api_polleria.dto.StockDTO;
import com.api_polleria.entity.Category;
import com.api_polleria.entity.Product;
import com.api_polleria.entity.ProductStoreStock;
import com.api_polleria.service.CategoryService;
import com.api_polleria.service.ConvertDTO;
import com.api_polleria.service.ProductService;
import com.api_polleria.service.ProductStoreStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductStoreStockService productStoreStockService;

    @Autowired
    private ConvertDTO convertDTO;

    private ResponseEntity<Page<ProductDTO>> createProductPageResponse(List<Product> productList, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), productList.size());
        List<Product> subList = productList.subList(start, end);
        Page<Product> productPage = new PageImpl<>(subList, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), productList.size());
        Page<ProductDTO> productDTOPage = productPage.map(convertDTO::convertToProductDTO);
        return ResponseEntity.status(HttpStatus.OK).body(productDTOPage);
    }

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String product,
            @RequestParam(required = false) String partialProduct,
            Pageable pageable) {

        if (product != null && !product.isEmpty()) {
            Product prod = productService.findByName(product);
            if (prod == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe un producto con ese nombre");
            }
            return createProductPageResponse(Collections.singletonList(prod), pageable);
        }

        List<Product> productList;

        if (partialProduct != null && !partialProduct.isEmpty()) {
            productList = productService.findByNameContaining(partialProduct);
        } else if (category != null && !category.isEmpty()) {
            productList = productService.findByCategoryList_Name(category);
        } else {
            Page<Product> productPage = productService.findAll(pageable);
            return ResponseEntity.status(HttpStatus.OK).body(productPage.map(convertDTO::convertToProductDTO));
        }

        if (category != null && !category.isEmpty()) {
            productList = productList.stream()
                    .filter(p -> p.getCategoryList().stream().anyMatch(c -> c.getName().equals(category)))
                    .collect(Collectors.toList());
        }

        return createProductPageResponse(productList, pageable);
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
