package com.api_polleria.repository;

import com.api_polleria.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Store findByName(String name);
}
