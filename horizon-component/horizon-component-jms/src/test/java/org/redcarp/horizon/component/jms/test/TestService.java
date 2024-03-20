package org.redcarp.horizon.component.jms.test;

import org.redcarp.horizon.component.jms.test.dto.UserRegisterMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author redcarp
 * @date 2024/3/1
 */
@Service
public class TestService {

	@Autowired
	JmsTemplate jmsTemplate;

	// spring.jta.enable=true ，required @Transactional
	@Transactional
	public void testMsg() {
		jmsTemplate.convertAndSend("destination_2", "hello");
	}

	// 不需要@Transactional注解，每次轮询监听都会开启一个新的事务！
	@JmsListener(destination = "destination_2")
	public void consume2(String msg) {
		System.out.println("consume2 = " + msg);
	}

	@JmsListener(destination = "horizon-system-user-register")
	public void consume2(UserRegisterMessage msg) {
		System.out.println("UserRegisterMessage = " + msg.toString());
	}

}
