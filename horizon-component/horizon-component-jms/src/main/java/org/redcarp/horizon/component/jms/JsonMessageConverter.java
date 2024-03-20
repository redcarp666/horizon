package org.redcarp.horizon.component.jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * 将java message bean转换为json.
 * <p>
 * {@link org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration JmsAutoConfiguration}自动将该bean配置到JMSTemplate中
 * 虽然{@link org.springframework.jms.support.converter.MappingJackson2MessageConverter
 * MappingJackson2MessageConverter}该类也有这种功能，但必须在消息中指定type，消息consumer才会根据该type进行convert，显然有点多余，因为我们用@JmsListener
 * 在方法中已经声明类型了
 */
@Component
public class JsonMessageConverter implements MessageConverter {

	/**
	 * 需要依赖spring-web包才能自动注入,see JacksonAutoConfiguration
	 */
	@Autowired
	private ObjectMapper mapper;


	@Override
	public Message toMessage(Object object, Session session) throws MessageConversionException, JMSException {
		String json;
		try {
			json = mapper.writeValueAsString(object);
		} catch (Exception e) {
			throw new MessageConversionException("Message cannot be parsed. ", e);
		}
		TextMessage message = session.createTextMessage();
		message.setText(json);
		return message;
	}


	@Override
	public Object fromMessage(Message message) throws JMSException {
		return ((TextMessage) message).getText();
	}
}