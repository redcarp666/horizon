package org.redcarp.horizon.infrastructure.configuration;

import org.redcarp.horizon.infrastructure.interceptor.LocalizationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 基础配置
 *
 * @author redcarp
 * @date 2024/2/19
 */
@Configuration
public class BaseWebMvcConfig implements WebMvcConfigurer {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LocalizationInterceptor());
	}
}
