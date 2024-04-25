package com.api_polleria.service;

import com.api_polleria.entity.Valoration;
import com.api_polleria.repository.ValorationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ValorationServiceImpl implements ValorationService{

    @Autowired
    private ValorationRepository valorationRepository;

    @Override
    public Optional<Valoration> findById(Long id) {
        return valorationRepository.findById(id);
    }

    @Override
    public Page<Valoration> findByProductId(Long productId, Pageable pageable) {
        return valorationRepository.findByProductId(productId, pageable);
    }

    @Override
    public Valoration save(Valoration valoration) {
        return valorationRepository.save(valoration);
    }

    @Override
    public void deleteById(Long id) {
        valorationRepository.deleteById(id);
    }
}
