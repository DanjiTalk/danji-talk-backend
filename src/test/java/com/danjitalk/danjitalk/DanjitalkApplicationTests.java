package com.danjitalk.danjitalk;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(S3ClientTestConfig.class)
class DanjitalkApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("allow-bean-definition-overriding: " +
		System.getProperty("spring.main.allow-bean-definition-overriding"));
	}

}
