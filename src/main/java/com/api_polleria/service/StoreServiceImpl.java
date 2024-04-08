package com.api_polleria.service;

import com.api_polleria.entity.Store;
import com.api_polleria.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Override
    public Page<Store> findAll(Pageable pageable) {
        return storeRepository.findAll(pageable);
    }

    @Override
    public Store findByName(String name) {
        return storeRepository.findByName(name);
    }

    @Override
    public Optional<Store> findById(Long id) {
        return storeRepository.findById(id);
    }

    @Override
    public Store save(Store store) {
        return storeRepository.save(store);
    }

    @Override
    public void deleteById(Long id) {
        storeRepository.deleteById(id);
    }
}
