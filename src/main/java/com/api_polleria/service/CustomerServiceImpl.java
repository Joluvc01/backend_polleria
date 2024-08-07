package com.api_polleria.service;

import com.api_polleria.entity.Address;
import com.api_polleria.entity.Customer;
import com.api_polleria.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Page<Customer> findAll(Specification<Customer> spec, Pageable pageable) {
        return customerRepository.findAll(spec, pageable);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    public boolean addressExists(Customer customer, Long addressId, Address newAddress) {
        return customer.getAddressList().stream()
                .filter(address -> !address.getId().equals(addressId))
                .anyMatch(address -> address.getAddress().equals(newAddress.getAddress()) &&
                        address.getDistrict().equals(newAddress.getDistrict()) &&
                        address.getProvince().equals(newAddress.getProvince()) &&
                        address.getState().equals(newAddress.getState()));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return customerRepository.findByEmail(email).orElseThrow();
    }
}
