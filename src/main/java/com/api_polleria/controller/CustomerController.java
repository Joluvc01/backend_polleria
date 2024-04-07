package com.api_polleria.controller;

import com.api_polleria.dto.CustomerDTO;
import com.api_polleria.dto.RegisterCustomerDTO;
import com.api_polleria.entity.Customer;
import com.api_polleria.service.ConvertDTO;
import com.api_polleria.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ConvertDTO convertDTO;

    @GetMapping
    public ResponseEntity<?> findAll(Pageable pageable){
        Page<Customer> customerPage = customerService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(customerPage.map(convertDTO::convertToCustomerDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        return customerService.findById(id)
                .map(customer -> ResponseEntity.ok(convertDTO.convertToCustomerDTO(customer)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody RegisterCustomerDTO customerDTO){
        Optional<Customer> optionalCustomer = customerService.findByEmail(customerDTO.getEmail());
        if(optionalCustomer.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El email ya esta en uso");
        }
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setLastname(customerDTO.getLastname());
        customer.setEmail(customerDTO.getEmail());
        customer.setPassword(customerDTO.getPassword());
        customer.setBirthdate(customerDTO.getBirthdate());
        customer.setStatus(true);
        System.out.println(customer);
        customerService.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cliente Creado");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CustomerDTO customerDTO){
        Optional<Customer> optionalCustomer = customerService.findById(id);
        if (optionalCustomer.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El cliente no existe");
        }
        Customer customer = optionalCustomer.get();
        customer.setName(customerDTO.getName());
        customer.setLastname(customerDTO.getLastname());
        customer.setEmail(customerDTO.getEmail());
        customer.setBirthdate(customerDTO.getBirthdate());
        customer.setStatus(customerDTO.getStatus());
        customerService.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cliente Actualizado");
    }

}
