package com.api_polleria.service;

import com.api_polleria.entity.Address;
import com.api_polleria.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface CustomerService extends UserDetailsService {

    List<Customer> findAllList();

    public Page<Customer> findAll(Pageable pageable);

    public Optional<Customer> findById(Long id);

    public Optional<Customer> findByEmail(String email);

    public Customer save(Customer customer);

    public void deleteById(Long id);

    public boolean addressExists(Customer customer, Long addressId, Address address);
}
