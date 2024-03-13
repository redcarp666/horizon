package org.redcarp.horizon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author redcarp
 * @date 2024/3/13
 */
@SpringBootApplication
@EnableFeignClients
public class ApiTestApplication {
	public static void main(String[] args) {
		SpringApplication sp = new SpringApplication(ApiTestApplication.class);
		sp.run(args);
	}
}
