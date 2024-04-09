package com.api_polleria.service;

import com.api_polleria.entity.Product;
import com.api_polleria.entity.Valoration;
import java.util.List;
import java.util.Optional;

public interface ValorationService {
    public List<Valoration> findAll();

    public Optional<Valoration> findById(Long id);

    public List<Valoration> findByProduct(Product product);

    public Valoration save(Valoration valoration);

    public void deleteById(Long id);
}
