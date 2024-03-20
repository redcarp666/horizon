package org.redcarp.horizon.component.jms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

/**
 * {@link org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver
 * HandlerMethodArgumentResolver}解析器将message解析出来转换为你的方法参数类型后，调用时传入到你用@JmsListener标注的方法中. 针对方法签名的不同参数类型，有不同的参数解析器。
 * 我们设置MessageConverter給DefaultMessageHandlerMethodFactory后，它将MessageConverter装饰成HandlerMethodArgumentResolver
 * 的某个实现类，解析出来的时候可以使用我们的设置的MessageConverter
 *
 * @author redcarp
 * @date 2024/3/19
 */
@EnableJms
@Configuration
public class JmsListenerConfig implements JmsListenerConfigurer {

	@Bean
	public DefaultMessageHandlerMethodFactory handlerMethodFactory() {
		DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
		factory.setMessageConverter(messagingConverter());
		return factory;
	}

	@Bean
	public MessageConverter messagingConverter() {
		return new MappingJackson2MessageConverter();
	}

	@Override
	public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
		registrar.setMessageHandlerMethodFactory(handlerMethodFactory());
	}
}