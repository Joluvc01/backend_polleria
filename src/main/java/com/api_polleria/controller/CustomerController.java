package com.api_polleria.controller;

import com.api_polleria.dto.CustomerDTO;
import com.api_polleria.entity.Address;
import com.api_polleria.entity.Customer;
import com.api_polleria.entity.Product;
import com.api_polleria.entity.Status;
import com.api_polleria.service.ConvertDTO;
import com.api_polleria.service.CustomerService;
import com.api_polleria.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final ProductService productService;
    private final ConvertDTO convertDTO;

    public CustomerController(CustomerService customerService, ProductService productService, ConvertDTO convertDTO) {
        this.customerService = customerService;
        this.productService = productService;
        this.convertDTO = convertDTO;
    }

    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(required = false) Status status,
                                                  @RequestParam(required = false) String name,
                                                  Pageable pageable) {

        Specification<Customer> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (name != null && !name.isEmpty()) {
                String pattern = "%" + name.toLowerCase() + "%";
                Expression<String> fullName = cb.concat(cb.concat(cb.lower(root.get("name")), " "), cb.lower(root.get("lastname")));
                predicates.add(cb.or(
                        cb.like(fullName, pattern)
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Customer> customerPage = customerService.findAll(spec, pageable);
        Page<CustomerDTO> dtoPage = customerPage.map(convertDTO::convertToCustomerDTO);
        return ResponseEntity.ok(dtoPage);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        return customerService.findById(id)
                .map(customer -> ResponseEntity.ok(convertDTO.convertToCustomerDTO(customer)))
                .orElse(ResponseEntity.notFound().build());
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
        customer.setStatus(optionalCustomer.get().getStatus());
        customerService.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cliente Actualizado");
    }

    @GetMapping("/{id}/purchases")
    public ResponseEntity<?> findPurchases(@PathVariable Long id){
        Optional<Customer> optionalCustomer = customerService.findById(id);
        if (optionalCustomer.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El cliente no existe");
        }
        Customer customer = optionalCustomer.get();
        return ResponseEntity.status(HttpStatus.OK).body(customer.getPurchaseList().stream().map(convertDTO::convertToPurchaseDTO).toList());
    }

    @GetMapping("/{id}/favorite-products")
    public ResponseEntity<?> findFavoriteProducts(@PathVariable Long id){
        Optional<Customer> optionalCustomer = customerService.findById(id);
        if (optionalCustomer.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El cliente no existe");
        }
        Customer customer = optionalCustomer.get();
        return ResponseEntity.status(HttpStatus.OK).body(customer.getFavoriteProducts().stream().map(convertDTO::convertToProductDTO).toList());
    }

    @PostMapping("/{id}/favorite-products/{productId}")
    public ResponseEntity<?> addFavoriteProduct(@PathVariable Long id, @PathVariable Long productId){
        Optional<Customer> optionalCustomer = customerService.findById(id);
        if (optionalCustomer.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El cliente no existe");
        }
        Optional<Product> optionalProduct = productService.findById(productId);
        if (optionalProduct.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto no existe");
        }
        Customer customer = optionalCustomer.get();
        Product product = optionalProduct.get();
        boolean productExists = customer.getFavoriteProducts().stream()
                .anyMatch(favoriteProduct -> favoriteProduct.getId().equals(product.getId()));
        if (productExists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El producto ya es favorito");
        }
        customer.getFavoriteProducts().add(product);
        customerService.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body("Producto Favorito Agregado");
    }

    @DeleteMapping("/{id}/favorite-products/{productId}")
    public ResponseEntity<?> deleteFavoriteProduct(@PathVariable Long id, @PathVariable Long productId){
        Optional<Customer> optionalCustomer = customerService.findById(id);
        if (optionalCustomer.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El cliente no existe");
        }
        Optional<Product> optionalProduct = productService.findById(productId);
        if (optionalProduct.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto no existe");
        }
        Customer customer = optionalCustomer.get();
        Product product = optionalProduct.get();
        customer.getFavoriteProducts().remove(product);
        customerService.save(customer);
        return ResponseEntity.status(HttpStatus.OK).body("Producto Favorito Eliminado");
    }

    @GetMapping("/{id}/address")
    public ResponseEntity<?> findAddress(@PathVariable Long id){
        Optional<Customer> optionalCustomer = customerService.findById(id);
        if (optionalCustomer.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El cliente no existe");
        }
        Customer customer = optionalCustomer.get();
        return ResponseEntity.status(HttpStatus.OK).body(customer.getAddressList());
    }

    @PostMapping("/{id}/address")
    public ResponseEntity<?> addAddress(@PathVariable Long id, @RequestBody Address address){
        Optional<Customer> optionalCustomer = customerService.findById(id);
        if (optionalCustomer.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El cliente no existe");
        }
        Customer customer = optionalCustomer.get();
        if (customerService.addressExists(customer, null, address)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La direccion ya existe");
        }
        address.setCustomer(customer);
        customer.getAddressList().add(address);
        customerService.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body("Direccion Agregada");
    }

    @PutMapping("/{id}/address/{addressId}")
    public ResponseEntity<?> updateAddress(@PathVariable Long id, @PathVariable Long addressId, @RequestBody Address address){
        Optional<Customer> optionalCustomer = customerService.findById(id);
        if (optionalCustomer.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El cliente no existe");
        }
        Customer customer = optionalCustomer.get();
        Optional<Address> optionalAddress = customer.getAddressList()
                .stream()
                .filter(addresses -> addresses.getId().equals(addressId))
                .findFirst();
        if (optionalAddress.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La direccion no existe");
        }
        Address addressToUpdate = optionalAddress.get();
        if (customerService.addressExists(customer, addressId, address)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La direccion ya existe");
        }
        addressToUpdate.setAddress(address.getAddress());
        addressToUpdate.setDistrict(address.getDistrict());
        addressToUpdate.setProvince(address.getProvince());
        addressToUpdate.setState(address.getState());
        customerService.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body("Direccion Actualizada");
    }

    @DeleteMapping("/{id}/address/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id, @PathVariable Long addressId){
        Optional<Customer> optionalCustomer = customerService.findById(id);
        if (optionalCustomer.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El cliente no existe");
        }
        Customer customer = optionalCustomer.get();
        Optional<Address> optionalAddress = customer.getAddressList()
                .stream()
                .filter(address -> address.getId().equals(addressId))
                .findFirst();
        if (optionalAddress.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La direccion no existe");
        }
        Address address = optionalAddress.get();
        customer.getAddressList().remove(address);
        customerService.save(customer);
        return ResponseEntity.status(HttpStatus.OK).body("Direccion Eliminada");
    }

    @PostMapping("/confirm/{id}")
    public ResponseEntity<?> confirmAccount(@PathVariable Long id){
        Optional<Customer> optionalCustomer = customerService.findById(id);
        if (optionalCustomer.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El cliente no existe");
        }
        Customer customer = optionalCustomer.get();
        customer.setStatus(Status.ACTIVE);
        customerService.save(customer);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario Confirmado");
    }

    @PostMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody String newPassword) {
        Optional<Customer> optionalCustomer = customerService.findById(id);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            if (Objects.equals(newPassword, customer.getPassword())){
                return ResponseEntity.badRequest().body("La nueva contraseña no puede ser igual a la contraseña actual");
            }

            customer.setPassword(newPassword);
            customerService.save(customer);
            return ResponseEntity.ok("Contraseña cambiada exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

}
