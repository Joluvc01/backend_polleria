package com.api_polleria.service;

import com.api_polleria.entity.Product;
import com.api_polleria.entity.Valoration;
import com.api_polleria.repository.ValorationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ValorationServiceImpl implements ValorationService{

    @Autowired
    private ValorationRepository valorationRepository;

    @Override
    public List<Valoration> findAll() {
        return valorationRepository.findAll();
    }

    @Override
    public Optional<Valoration> findById(Long id) {
        return valorationRepository.findById(id);
    }

    @Override
    public List<Valoration> findByProduct(Product product) {
        return valorationRepository.findByProduct(product);
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
