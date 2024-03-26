package com.api_polleria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})  //Exclusion de Spring Security
public class ApiPolleriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiPolleriaApplication.class, args);
	}

}
