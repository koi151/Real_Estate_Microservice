package com.koi151.mspropertycategory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsPropertyCategoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsPropertyCategoryApplication.class, args);
	}

}
