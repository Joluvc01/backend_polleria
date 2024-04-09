package com.api_polleria.controller;

import com.api_polleria.dto.ValorationDTO;
import com.api_polleria.entity.Customer;
import com.api_polleria.entity.Product;
import com.api_polleria.entity.Valoration;
import com.api_polleria.service.ConvertDTO;
import com.api_polleria.service.CustomerService;
import com.api_polleria.service.ProductService;
import com.api_polleria.service.ValorationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/valorations")
public class ValorationController {

    @Autowired
    private ValorationService valorationService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ConvertDTO convertDTO;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(valorationService.findAll()
                .stream()
                .map(convertDTO::convertToValorationDTO)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return valorationService.findById(id)
                .map(valoration -> ResponseEntity.ok(convertDTO.convertToValorationDTO(valoration)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> findByProductId(@PathVariable Long id) {
        Optional<Product> optionalValoration = productService.findById(id);
        if (optionalValoration.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto no existe");
        }

        List<Valoration> valorations = valorationService.findByProduct(optionalValoration.get());
        return ResponseEntity.ok(valorations);
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody ValorationDTO valorationDTO) {
        Optional<Product> optionalProduct = productService.findById(valorationDTO.getProductId());
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto no existe");
        }
        Optional<Customer> optionalCustomer = customerService.findById(valorationDTO.getCustomerId());
        if (optionalCustomer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El cliente no existe");
        }
        Valoration valoration = new Valoration();
        valoration.setValoration(valorationDTO.getValoration());
        valoration.setReview(valorationDTO.getReview());
        LocalDate date = LocalDate.now(ZoneId.of("America/Lima"));
        valoration.setDate(date);
        valoration.setProduct(optionalProduct.get());
        valoration.setCustomer(optionalCustomer.get());
        valorationService.save(valoration);
        return ResponseEntity.ok("Valoracion Creada");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ValorationDTO valorationDTO) {
        Optional<Valoration> optionalValoration = valorationService.findById(id);
        if (optionalValoration.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La Valoracion no existe");
        }
        Optional<Product> optionalProduct = productService.findById(valorationDTO.getProductId());
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto no existe");
        }
        Optional<Customer> optionalCustomer = customerService.findById(valorationDTO.getCustomerId());
        if (optionalCustomer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El cliente no existe");
        }
        Valoration valorationUpdate = optionalValoration.get();
        valorationUpdate.setValoration(valorationDTO.getValoration());
        valorationUpdate.setReview(valorationDTO.getReview());
        valorationUpdate.setProduct(optionalProduct.get());
        valorationUpdate.setCustomer(optionalCustomer.get());
        valorationService.save(valorationUpdate);
        return ResponseEntity.ok("Valoracion Actualizada");
    }

}
