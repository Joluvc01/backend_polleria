package com.api_polleria.controller;

import com.api_polleria.dto.AddressDTO;
import com.api_polleria.dto.CustomerDTO;
import com.api_polleria.dto.RegisterCustomerDTO;
import com.api_polleria.entity.Address;
import com.api_polleria.entity.Customer;
import com.api_polleria.entity.Product;
import com.api_polleria.service.ConvertDTO;
import com.api_polleria.service.CustomerService;
import com.api_polleria.service.ProductService;
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
    private ProductService productService;

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
    public ResponseEntity<?> addAddress(@PathVariable Long id, @RequestBody AddressDTO addressDTO){
        Optional<Customer> optionalCustomer = customerService.findById(id);
        if (optionalCustomer.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El cliente no existe");
        }
        Customer customer = optionalCustomer.get();
        boolean addressExists = customer.getAddressList().stream()
                .anyMatch(address -> address.getAddress().equals(addressDTO.getAddress()) &&
                        address.getDistrict().equals(addressDTO.getDistrict()) &&
                        address.getProvince().equals(addressDTO.getProvince()) &&
                        address.getState().equals(addressDTO.getState()));
        if (addressExists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La direccion ya existe");
        }
        Address newAddress = new Address();
        newAddress.setAddress(addressDTO.getAddress());
        newAddress.setDistrict(addressDTO.getDistrict());
        newAddress.setProvince(addressDTO.getProvince());
        newAddress.setState(addressDTO.getState());
        newAddress.setCustomer(customer);
        customer.getAddressList().add(newAddress);
        customerService.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body("Direccion Agregada");
    }

    @PutMapping("/{id}/address/{addressId}")
    public ResponseEntity<?> updateAddress(@PathVariable Long id, @PathVariable Long addressId, @RequestBody AddressDTO addressDTO){
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
        Address addressToUpdate = optionalAddress.get();
        boolean addressExists = customer.getAddressList().stream()
                .filter(address -> !address.getId().equals(addressId))
                .anyMatch(address -> address.getAddress().equals(addressDTO.getAddress()) &&
                        address.getDistrict().equals(addressDTO.getDistrict()) &&
                        address.getProvince().equals(addressDTO.getProvince()) &&
                        address.getState().equals(addressDTO.getState()));
        if (addressExists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La direccion ya existe");
        }
        addressToUpdate.setAddress(addressDTO.getAddress());
        addressToUpdate.setDistrict(addressDTO.getDistrict());
        addressToUpdate.setProvince(addressDTO.getProvince());
        addressToUpdate.setState(addressDTO.getState());
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

}
