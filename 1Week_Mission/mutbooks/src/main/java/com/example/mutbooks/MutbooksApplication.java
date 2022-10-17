package com.example.mutbooks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MutbooksApplication {

	public static void main(String[] args) {
		SpringApplication.run(MutbooksApplication.class, args);
	}

}
