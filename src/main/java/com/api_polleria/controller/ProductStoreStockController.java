package com.api_polleria.controller;

import com.api_polleria.dto.ProductDTO;
import com.api_polleria.dto.ProductStoreStockDTO;
import com.api_polleria.entity.Product;
import com.api_polleria.entity.ProductStoreStock;
import com.api_polleria.entity.Store;
import com.api_polleria.service.ConvertDTO;
import com.api_polleria.service.ProductService;
import com.api_polleria.service.ProductStoreStockService;
import com.api_polleria.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stock")
public class ProductStoreStockController {

    @Autowired
    private ProductService productService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private ProductStoreStockService productStoreStockService;

    @Autowired
    private ConvertDTO convertDTO;

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<ProductStoreStock> productStoreStocks = productStoreStockService.findAll();
        List<ProductStoreStockDTO> productStoreStockDTOS = productStoreStocks.stream()
                .map(convertDTO::convertToProductStoreStockDTO)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(productStoreStockDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        return productStoreStockService.findById(id)
                .map(store -> ResponseEntity.ok(convertDTO.convertToProductStoreStockDTO(store)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> findStocksByProduct(@PathVariable Long productId){
        Optional<Product> optionalProduct = productService.findById(productId);
        if(optionalProduct.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto no existe");
        }
        List<ProductStoreStock> productStoreStocks = productStoreStockService.findByProduct(optionalProduct.get());
        List<ProductStoreStockDTO> productStoreStockDTOS = productStoreStocks.stream()
                .map(convertDTO::convertToProductStoreStockDTO)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(productStoreStockDTOS);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductStoreStockDTO productStoreStockDTO) {
        Optional<Product> optionalProduct = productService.findById(productStoreStockDTO.getProduct());
        Optional<Store> storeOptional = storeService.findById(productStoreStockDTO.getStore());
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto no existe");
        } else if (storeOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La tienda no existe");
        }
        Product product = optionalProduct.get();
        Store store = storeOptional.get();

        ProductStoreStock addStock = productStoreStockService.findByProductAndStore(product,store);
        if (addStock != null) {
            addStock.setQuantity(productStoreStockDTO.getQuantity() + addStock.getQuantity());
            productStoreStockService.save(addStock);
        } else {
            ProductStoreStock storeStock = new ProductStoreStock();
            storeStock.setProduct(product);
            storeStock.setStore(store);
            storeStock.setQuantity(productStoreStockDTO.getQuantity());
            productStoreStockService.save(storeStock);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Stock Agregado");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductStoreStockDTO productStoreStockDTO) {
        Optional<ProductStoreStock> optionalProductStoreStock = productStoreStockService.findById(id);
        if (optionalProductStoreStock.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stock inexistente");
        }
        ProductStoreStock storeStock = optionalProductStoreStock.get();
        storeStock.setQuantity(productStoreStockDTO.getQuantity());
        productStoreStockService.save(storeStock);

        return ResponseEntity.status(HttpStatus.CREATED).body("Stock Actualizado");
    }

}
