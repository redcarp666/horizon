package org.redcarp.horizon.component.jms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author redcarp
 * @date 2024/2/29
 */
@SpringBootApplication
public class TestJmsApplication {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TestJmsApplication.class);
		app.run(args);
	}
}
