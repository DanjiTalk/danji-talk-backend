package com.danjitalk.danjitalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DanjitalkApplication {

	public static void main(String[] args) {
		SpringApplication.run(DanjitalkApplication.class, args);
	}

}
