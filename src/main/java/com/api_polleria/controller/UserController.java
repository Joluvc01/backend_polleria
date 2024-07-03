package com.api_polleria.controller;

import com.api_polleria.dto.UserDTO;
import com.api_polleria.entity.User;
import com.api_polleria.service.ConvertDTO;
import com.api_polleria.service.UserService;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


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
    public ResponseEntity<?> findAll(@RequestParam(required = false) Boolean status,
                                     @RequestParam(required = false) String name,
                                     Pageable pageable) {

        Specification<User> spec = (root, query, cb) -> {
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

        Page<User> userPage = userService.findAll(spec, pageable);
        Page<UserDTO> userDTOPage = userPage.map(convertDTO::convertToUserDTO);
        return ResponseEntity.ok(userDTOPage);
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
        newuser.setPassword(user.getPassword());
        newuser.setName(user.getName());
        newuser.setLastname(user.getLastname());
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
        existingUser.setName(userDTO.getName());
        existingUser.setLastname(userDTO.getLastname());
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

    @PostMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody String newPassword) {
        Optional<User> optionalUser = userService.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (Objects.equals(newPassword, user.getPassword())) {
                return ResponseEntity.badRequest().body("La nueva contraseña no puede ser igual a la contraseña actual");
            }

            user.setPassword(newPassword);
            userService.save(user);

            return ResponseEntity.ok("Contraseña cambiada exitosamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }
}

