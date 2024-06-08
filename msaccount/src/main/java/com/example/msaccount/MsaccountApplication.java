package com.example.msaccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsaccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsaccountApplication.class, args);
	}

}
