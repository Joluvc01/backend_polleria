package com.api_polleria.service;

import com.api_polleria.entity.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PurchaseService {

    public Page<Purchase> findAll(Pageable pageable);

    public Optional<Purchase> findById(Long id);

    public Purchase save(Purchase purchase);

    public void deleteById(Long id);
}
