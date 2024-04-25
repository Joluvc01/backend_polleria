package com.api_polleria.controller;

import com.api_polleria.dto.ValorationDTO;
import com.api_polleria.entity.Customer;
import com.api_polleria.entity.Product;
import com.api_polleria.entity.Valoration;
import com.api_polleria.service.ConvertDTO;
import com.api_polleria.service.CustomerService;
import com.api_polleria.service.ProductService;
import com.api_polleria.service.ValorationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

@RestController
@RequestMapping("/valorations")
public class ValorationController {

    private final ValorationService valorationService;

    private final ProductService productService;

    private final CustomerService customerService;

    private final ConvertDTO convertDTO;

    public ValorationController(ValorationService valorationService, ProductService productService, CustomerService customerService, ConvertDTO convertDTO) {
        this.valorationService = valorationService;
        this.productService = productService;
        this.customerService = customerService;
        this.convertDTO = convertDTO;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> findByProductId(@PathVariable Long productId, Pageable pageable) {
        Page<Valoration> valorationPage = valorationService.findByProductId(productId, pageable);
        return ResponseEntity.ok(valorationPage.map(convertDTO::convertToValorationDTO));
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
