package com.api_polleria.controller;

import com.api_polleria.dto.PurchaseDTO;
import com.api_polleria.dto.Purchase_detailDTO;
import com.api_polleria.entity.Customer;
import com.api_polleria.entity.Product;
import com.api_polleria.entity.Purchase;
import com.api_polleria.entity.Purchase_detail;
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
import java.util.*;

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
    public ResponseEntity<?> save(@RequestBody PurchaseDTO purchaseDTO) {
        Optional<Customer> optionalCustomer = customerService.findById(purchaseDTO.getCustomer());
        if (optionalCustomer.isEmpty()) {
            return ResponseEntity.badRequest().body("El cliente no existe");
        }
        Purchase purchase = new Purchase();
        LocalDate date = LocalDate.now(ZoneId.of("America/Lima"));
        purchase.setDate(date);
        purchase.setCustomer(optionalCustomer.get());

        Map<Long, Purchase_detail> purchaseDetailMap = new HashMap<>();
        double total = 0;

        for (Purchase_detailDTO detailDTO : purchaseDTO.getDetails()) {
            Optional<Product> optionalProduct = productService.findById(detailDTO.getProduct());
            if (optionalProduct.isEmpty()) {
                return ResponseEntity.badRequest().body("El producto no existe");
            }

            Product product = optionalProduct.get();
            Long productId = product.getId();
            double productPrice = product.getPrice();
            double detailTotal = productPrice * detailDTO.getQuantity();
            total += detailTotal;

            Purchase_detail purchaseDetail;
            if (purchaseDetailMap.containsKey(productId)) {
                purchaseDetail = purchaseDetailMap.get(productId);
                purchaseDetail.setQuantity(purchaseDetail.getQuantity() + detailDTO.getQuantity());
                purchaseDetail.setTotal(purchaseDetail.getTotal() + detailTotal);
            } else {
                purchaseDetail = new Purchase_detail();
                purchaseDetail.setProduct(product);
                purchaseDetail.setQuantity(detailDTO.getQuantity());
                purchaseDetail.setTotal(detailTotal);
                purchaseDetail.setPurchase(purchase);
                purchaseDetailMap.put(productId, purchaseDetail);
            }
        }

        double igv = total * 0.18;
        double subtotal = total - igv;
        purchase.setTotal(total);
        purchase.setIgv(igv);
        purchase.setSubtotal(subtotal);
        purchase.setDetails(new ArrayList<>(purchaseDetailMap.values()));
        purchaseService.save(purchase);
        return ResponseEntity.ok("Compra guardada exitosamente");
    }

}
