package com.project.banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BankingCitadCodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingCitadCodeApplication.class, args);
	}

}
