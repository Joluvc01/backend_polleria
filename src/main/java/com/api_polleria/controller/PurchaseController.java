package com.api_polleria.controller;

import com.api_polleria.dto.PurchaseDTO;
import com.api_polleria.entity.Customer;
import com.api_polleria.entity.Purchase;
import com.api_polleria.service.ConvertDTO;
import com.api_polleria.service.CustomerService;
import com.api_polleria.service.ProductService;
import com.api_polleria.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ConvertDTO convertDTO;

    @GetMapping
    public ResponseEntity<?> findAll(Pageable pageable){
        Page<Purchase> purchasePage = purchaseService.findAll(pageable);
        return ResponseEntity.ok().body(purchasePage.map(convertDTO::convertToPurchaseDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        return purchaseService.findById(id)
                .map(purchase -> ResponseEntity.ok().body(convertDTO.convertToPurchaseDTO(purchase)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody PurchaseDTO purchaseDTO){
        Optional<Customer> optionalCustomer = customerService.findById(purchaseDTO.getCustomer());
        if (optionalCustomer.isEmpty()){
            return ResponseEntity.badRequest().body("El cliente no existe");
        }
        Purchase purchase = new Purchase();
        LocalDate date = LocalDate.now(ZoneId.of("America/Lima"));
        purchase.setDate(date);
        purchase.setCustomer(optionalCustomer.get());

        return null;
    }
}
