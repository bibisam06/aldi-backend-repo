package com.bibisam06.aldi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class AldiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AldiApplication.class, args);
	}

}
