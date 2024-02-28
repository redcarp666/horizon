package org.redcarp.common.base.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * 异常消息来源文件配置类
 *
 * @author redcarp
 * @date 2024/2/19
 */
@Configuration
public class MessageSourceConfig {

	public static final String BASENAME = "i18n/messages";
	public static final String ENCODING = "UTF-8";

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename(BASENAME);
		messageSource.setDefaultEncoding(ENCODING);
		return messageSource;
	}
}
