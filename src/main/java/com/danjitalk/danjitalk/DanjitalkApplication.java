package com.danjitalk.danjitalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableJpaAuditing
@EnableMongoAuditing
@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.danjitalk.danjitalk.infrastructure.mongo")
@EnableJpaRepositories(basePackages = "com.danjitalk.danjitalk.infrastructure.repository")
public class DanjitalkApplication {

	public static void main(String[] args) {
		SpringApplication.run(DanjitalkApplication.class, args);
	}

}
