package com.api_polleria.service;

import com.api_polleria.entity.Valoration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface ValorationService {

    public Optional<Valoration> findById(Long id);
    public Page<Valoration> findByProductId(Long productId, Pageable pageable);

    public Valoration save(Valoration valoration);

    public void deleteById(Long id);
}
