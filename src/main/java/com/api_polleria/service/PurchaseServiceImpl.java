package com.api_polleria.service;

import com.api_polleria.entity.Purchase;
import com.api_polleria.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PurchaseServiceImpl implements PurchaseService{

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public Page<Purchase> findAll(Specification<Purchase> spec, Pageable pageable) {
        return purchaseRepository.findAll(pageable);
    }

    @Override
    public Optional<Purchase> findById(Long id) {
        return purchaseRepository.findById(id);
    }

    @Override
    public Purchase save(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    @Override
    public void deleteById(Long id) {
        purchaseRepository.deleteById(id);
    }
}
