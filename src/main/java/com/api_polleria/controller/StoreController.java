package com.api_polleria.controller;

import com.api_polleria.entity.Store;
import com.api_polleria.service.StoreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RestController
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(required = false) Boolean status,
            Pageable pageable) {

        Specification<Store> spec = (root, query, cb) -> {
            if (status == null) {
                return cb.isTrue(cb.literal(true));
            } else {
                return cb.equal(root.get("status"), status);
            }
        };

        Page<Store> storePage = storeService.findAll(spec, pageable);
        return ResponseEntity.ok(storePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return storeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Store store) {
        Store exist = storeService.findByName(store.getName());
        if (exist != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La tienda ya existe");
        }
        store.setStatus(true);
        storeService.save(store);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tienda Creada");

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Store store) {
        Optional<Store> optionalCategory = storeService.findById(id);
        if (optionalCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La tienda no existe");
        }
        store.setId(id);
        storeService.save(store);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tienda Actualizada");

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        Optional<Store> optionalCategory = storeService.findById(id);
        if (optionalCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La tienda no existe");
        }
        storeService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Tienda Eliminada");

    }
}
