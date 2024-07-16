package com.koi151.ms_post_approval;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
@EnableFeignClients
public class PropertySubmissionApplication {

	public static void main(String[] args) {
		SpringApplication.run(PropertySubmissionApplication.class, args);
	}

}
