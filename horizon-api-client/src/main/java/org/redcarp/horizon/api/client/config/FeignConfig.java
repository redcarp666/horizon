package org.redcarp.horizon.api.client.config;

import feign.RequestInterceptor;
import org.redcarp.horizon.core.security.SecurityUserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author redcarp
 * @date 2024/3/13
 */
@Configuration
public class FeignConfig {
	@Autowired(required = false)
	SecurityUserHolder userHolder;

	@Bean
	public RequestInterceptor requestInterceptor() {
		return requestTemplate -> {
			if (userHolder != null) {
				String currentToken = userHolder.getCurrentToken();
				if (currentToken != null) {
					requestTemplate.header("Authorization", "Bearer " + currentToken);
				}
			}

		};
	}
}
