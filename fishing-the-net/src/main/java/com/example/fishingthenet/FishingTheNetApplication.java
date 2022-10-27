package com.example.fishingthenet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class FishingTheNetApplication {

	public static void main(String[] args) {
		SpringApplication.run(FishingTheNetApplication.class, args);
	}

}
