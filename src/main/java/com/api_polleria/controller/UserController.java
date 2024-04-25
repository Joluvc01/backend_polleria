package com.api_polleria.controller;

import com.api_polleria.dto.UserDTO;
import com.api_polleria.entity.User;
import com.api_polleria.service.ConvertDTO;
import com.api_polleria.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final ConvertDTO convertDTO;

    public UserController(UserService userService, ConvertDTO convertDTO) {
        this.userService = userService;
        this.convertDTO = convertDTO;
    }

    @GetMapping()
    public ResponseEntity<?> findAll() {
        List<User> users = userService.findAll();
        List<UserDTO> userDTOs = users.stream()
                .map(convertDTO::convertToUserDTO)
                .toList();
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(convertDTO.convertToUserDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody User user) {
        Optional<User> existingUser = userService.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre de usuario ya está en uso");
        }

        User newuser = new User();
        newuser.setUsername(user.getUsername());
//        newuser.setPassword(passwordEncoder.encode(user.getPassword()));
        newuser.setPassword(user.getPassword());
        newuser.setFullname(user.getFullname());
        newuser.setStatus(true);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario Creado");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        Optional<User> optionalUser = userService.findById(id);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        User existingUser = optionalUser.get();
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setFullname(userDTO.getFullname());
        existingUser.setStatus(userDTO.getStatus());
        return ResponseEntity.ok("Usuario Actualizado");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        Optional<User> optionalUser = userService.findById(id);

        if(optionalUser.isPresent()){
            userService.deleteById(id);
            return ResponseEntity.ok("Usuario eliminado");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

//    @PostMapping("/change-password/{id}")
//    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody String newPassword) {
//        Optional<User> optionalUser = userService.findById(id);
//
//        if (optionalUser.isPresent()) {
//            User user = optionalUser.get();
//
//            if (passwordEncoder.matches(newPassword, user.getPassword())) {
//                return ResponseEntity.badRequest().body("La nueva contraseña no puede ser igual a la contraseña actual");
//            }
//
//            user.setPassword(passwordEncoder.encode(newPassword));
//            userService.save(user);
//
//            return ResponseEntity.ok("Contraseña cambiada exitosamente");
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
//        }
//    }
}

