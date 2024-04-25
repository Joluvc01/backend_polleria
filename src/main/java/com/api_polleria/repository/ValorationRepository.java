package com.api_polleria.repository;

import com.api_polleria.entity.Valoration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ValorationRepository extends JpaRepository<Valoration, Long>{
    Page<Valoration> findByProductId(Long productId, Pageable pageable);
}
