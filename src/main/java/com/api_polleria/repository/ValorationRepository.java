package com.api_polleria.repository;

import com.api_polleria.entity.Product;
import com.api_polleria.entity.Valoration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ValorationRepository extends JpaRepository<Valoration, Long>{
    List<Valoration> findByProduct(Product product);
}
