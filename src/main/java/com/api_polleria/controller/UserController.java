package com.api_polleria.controller;

import com.api_polleria.dto.UserDTO;
import com.api_polleria.entity.User;
import com.api_polleria.service.ConvertDTO;
import com.api_polleria.service.UserService;
import com.api_polleria.service.UtilsService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ConvertDTO convertDTO;
    private final UtilsService utilsService;

    public UserController(UserService userService, ConvertDTO convertDTO, UtilsService utilsService) {
        this.userService = userService;
        this.convertDTO = convertDTO;
        this.utilsService = utilsService;
    }

    @GetMapping()
    public ResponseEntity<?> findAll(@RequestParam(required = false) Boolean status,
                                     Pageable pageable) {
        List<User> userList = userService.findAll(pageable).getContent();

        if (status != null) {
            userList = userList.stream()
                    .filter(user -> user.getStatus().equals(status))
                    .toList();
        }

        return utilsService.createPageResponse(userList, pageable, Function.identity());

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
        userService.save(newuser);
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
        userService.save(existingUser);
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

