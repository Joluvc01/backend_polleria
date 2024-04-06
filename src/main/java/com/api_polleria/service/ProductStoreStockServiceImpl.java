package com.api_polleria.service;

import com.api_polleria.entity.Product;
import com.api_polleria.entity.ProductStoreStock;
import com.api_polleria.entity.Store;
import com.api_polleria.repository.ProductStoreStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductStoreStockServiceImpl implements ProductStoreStockService{

    @Autowired
    private ProductStoreStockRepository productStoreStockRepository;

    @Override
    public List<ProductStoreStock> findAll() {
        return productStoreStockRepository.findAll();
    }

    @Override
    public Optional<ProductStoreStock> findById(Long id) {
        return productStoreStockRepository.findById(id);
    }

    @Override
    public ProductStoreStock findByProductAndStore(Product product, Store store) {
        return productStoreStockRepository.findByProductAndStore(product, store);
    }

    @Override
    public List<ProductStoreStock> findByProduct(Product product) {
        return productStoreStockRepository.findByProduct(product);
    }

    @Override
    public ProductStoreStock save(ProductStoreStock storeStock) {
        return productStoreStockRepository.save(storeStock);
    }

    @Override
    public void deleteById(Long id) {
        productStoreStockRepository.deleteById(id);
    }
}
