package com.api_polleria.service;

import com.api_polleria.entity.Product;
import com.api_polleria.entity.ProductStoreStock;
import com.api_polleria.entity.Store;

import java.util.List;
import java.util.Optional;

public interface ProductStoreStockService {

    public List<ProductStoreStock> findAll();

    public Optional<ProductStoreStock> findById(Long id);

    ProductStoreStock findByProductAndStore(Product product, Store store);

    List<ProductStoreStock> findByProduct(Product product);

    public ProductStoreStock save(ProductStoreStock storeStock);

    public void deleteById(Long id);
}
