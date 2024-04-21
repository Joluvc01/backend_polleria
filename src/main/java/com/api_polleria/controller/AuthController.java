package com.api_polleria.controller;

import com.api_polleria.dto.RegisterCustomerDTO;
import com.api_polleria.dto.ReqRes;
import com.api_polleria.entity.Customer;
import com.api_polleria.entity.Status;
import com.api_polleria.service.AuthService;
import com.api_polleria.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerService customerService;

    @PostMapping("/user")
    public ResponseEntity<?> loginUser(@RequestBody ReqRes reqRes){
        ReqRes response = authService.signInUser(reqRes);
        if(response.getStatusCode()==200){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
    }

    @PostMapping("/customer")
    public ResponseEntity<?> loginCustomer(@RequestBody ReqRes reqRes){
        ReqRes response = authService.signInCustomer(reqRes);
        if(response.getStatusCode()==200){
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
    }

    @PostMapping("/singup")
    public ResponseEntity<?> create(@RequestBody RegisterCustomerDTO customerDTO){
        Optional<Customer> optionalCustomer = customerService.findByEmail(customerDTO.getEmail());
        if(optionalCustomer.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El email ya esta en uso");
        }
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setLastname(customerDTO.getLastname());
        customer.setEmail(customerDTO.getEmail());
        customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        customer.setBirthdate(customerDTO.getBirthdate());
        customer.setStatus(Status.ACTIVE);
        customerService.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cliente Creado");
    }

    @PostMapping("/recovery")
    public ResponseEntity<?> recovery(@RequestBody String email){
        Optional<Customer> customer = customerService.findByEmail(email);
        if(customer.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El email no esta registrado");
        }
        //Falta crear metodo de envio de correo
        return ResponseEntity.ok("Se le envio un correo para recuperar su contraseña");
    }
}
