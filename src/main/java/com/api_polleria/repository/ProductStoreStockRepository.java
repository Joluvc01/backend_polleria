package com.api_polleria.repository;

import com.api_polleria.entity.Product;
import com.api_polleria.entity.ProductStoreStock;
import com.api_polleria.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductStoreStockRepository extends JpaRepository<ProductStoreStock, Long> {
    ProductStoreStock findByProductAndStore(Product product, Store store);



    List<ProductStoreStock> findByProduct(Product product);
}
