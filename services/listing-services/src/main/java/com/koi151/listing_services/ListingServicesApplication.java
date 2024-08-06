package com.koi151.listing_services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class ListingServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ListingServicesApplication.class, args);
	}

}
