package com.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages ="com.springboot")
@EnableJpaRepositories(basePackages = "com.springboot")
public class ShipperApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShipperApiApplication.class, args);
		
		
	}
}