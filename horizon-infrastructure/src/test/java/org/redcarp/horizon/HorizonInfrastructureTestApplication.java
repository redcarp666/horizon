package org.redcarp.horizon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author redcarp
 * @date 2024/3/11
 */
@SpringBootApplication
public class HorizonInfrastructureTestApplication {
	public static void main(String[] args) {
		SpringApplication sp = new SpringApplication(HorizonInfrastructureTestApplication.class);
		sp.run(args);
	}
}
