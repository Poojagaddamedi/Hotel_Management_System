package com.hotel.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.hotel.erp.entity")
@EnableJpaRepositories("com.hotel.erp.repository")
public class HotelManagementErpApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelManagementErpApplication.class, args);
	}
}
