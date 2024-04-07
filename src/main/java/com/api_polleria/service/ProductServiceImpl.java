package com.api_polleria.service;

import com.api_polleria.entity.Product;
import com.api_polleria.entity.Valoration;
import com.api_polleria.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<Product> findAll(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        List<Product> products = productPage.getContent();
        for (Product product : products) {
            updateAverageValoration(product);
        }
        return new PageImpl<>(products, pageable, productPage.getTotalElements());
    }

    @Override
    public Optional<Product> findById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(this::updateAverageValoration);
        return optionalProduct;
    }

    @Override
    public Product findByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> findByNameContaining(String contain) {
        return productRepository.findByNameContaining(contain);
    }

    @Override
    public List<Product> findByCategoryList_Name(String category) {
        return productRepository.findByCategoryList_Name(category);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    private void updateAverageValoration(Product product) {
        List<Valoration> valorationList = product.getValorationList();
        if (valorationList != null && !valorationList.isEmpty()) {
            double sum = 0.0;
            for (Valoration valoration : valorationList) {
                sum += valoration.getValoration();
            }
            double average = sum / valorationList.size();
            product.setValoration(average);
        } else {
            product.setValoration(0.0); // Si no hay valoraciones, establece el promedio como 0
        }
    }
}
