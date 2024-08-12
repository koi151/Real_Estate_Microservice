package com.koi151.msproperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients
@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
@EnableCaching
public class MspropertiesApplication {
	public static void main(String[] args) {
		SpringApplication.run(MspropertiesApplication.class, args);
	}
}
