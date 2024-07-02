package com.api_polleria.tasks;

import com.api_polleria.entity.User;
import com.api_polleria.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    private final UserService userService;

    @Autowired
    public AdminUserInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        if (userService.findByUsername("admin").isEmpty()) {
            User adminUser = new User();
            adminUser.setUsername("admin");
//            adminUser.setPassword(passwordEncoder.encode("admin"));
            adminUser.setPassword("admin");
            adminUser.setName("Admi");
            adminUser.setLastname("Nistrador");
            adminUser.setStatus(true);
            userService.save(adminUser);
            System.out.println("Admin user created successfully.");
        }
    }
}
