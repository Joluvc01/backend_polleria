package com.api_polleria.service;

import com.api_polleria.dto.ReqRes;
import com.api_polleria.entity.Customer;
import com.api_polleria.entity.User;
import com.api_polleria.jwt.JWTUtils;
import com.api_polleria.repository.CustomerRepository;
import com.api_polleria.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ReqRes signInUser(ReqRes singInRequest){
        ReqRes response = new ReqRes();
        try {
            authenticationManager.authenticate((new UsernamePasswordAuthenticationToken(singInRequest.getUsername(), singInRequest.getPassword())));
            var user = userRepository.findByUsername(singInRequest.getUsername()).orElseThrow();
            var jwt = jwtUtils.generateUserToken(user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setMessage("Exito al ingresar");
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                response.setError("Contraseña incorrecta");
            } else {
                Optional<User> optionalUser = userRepository.findByUsername(singInRequest.getUsername());
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    if (!user.isEnabled()) {
                        response.setError("Usuario Desactivado");
                    }
                } else {
                    response.setError("Credenciales Inválidas");
                }
            }
            response.setStatusCode(401);
        }
        return response;
    }

    public ReqRes signInCustomer(ReqRes singInRequest){
        ReqRes response = new ReqRes();
        try {
            authenticationManager.authenticate((new UsernamePasswordAuthenticationToken(singInRequest.getEmail(), singInRequest.getPassword())));
            var customer = customerRepository.findByEmail(singInRequest.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateCustomerToken(customer, customer);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setMessage("Exito al ingresar");
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                response.setError("Contraseña incorrecta");
            } else {
                Optional<Customer> optionalUser = customerRepository.findByEmail(singInRequest.getEmail());
                if (optionalUser.isPresent()) {
                    Customer customer = optionalUser.get();
                    if (!customer.isEnabled()) {
                        response.setError("Usuario Desactivado");
                    }
                } else {
                    response.setError("Credenciales Inválidas");
                }
            }
            response.setStatusCode(401);
        }
        return response;
    }
}

