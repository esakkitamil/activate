package com.niq.activate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class NiqActivateApplication {

	public static void main(String[] args) {
		SpringApplication.run(NiqActivateApplication.class, args);
	}
}
