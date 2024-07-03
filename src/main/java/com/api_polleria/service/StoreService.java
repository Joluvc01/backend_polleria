package com.api_polleria.service;

import com.api_polleria.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface StoreService {

    public Page<Store> findAll(Specification<Store> spec, Pageable pageable);

    public Store findByName(String name);

    public Optional<Store> findById(Long id);

    public Store save(Store store);

    public void deleteById(Long id);
}
