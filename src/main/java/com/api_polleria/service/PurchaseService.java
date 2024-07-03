package com.api_polleria.service;

import com.api_polleria.entity.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface PurchaseService {

    public Page<Purchase> findAll(Specification<Purchase> spec, Pageable pageable);

    public Optional<Purchase> findById(Long id);

    public Purchase save(Purchase purchase);

    public void deleteById(Long id);
}
