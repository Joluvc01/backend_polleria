package com.api_polleria.service;

import com.api_polleria.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CustomerService {
    public Page<Customer> findAll(Pageable pageable);

    public Optional<Customer> findById(Long id);

    public Optional<Customer> findByEmail(String email);

    public Customer save(Customer customer);

    public void deleteById(Long id);
}
