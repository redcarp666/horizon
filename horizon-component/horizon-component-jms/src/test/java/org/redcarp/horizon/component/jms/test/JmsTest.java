package org.redcarp.horizon.component.jms.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;

/**
 * @author redcarp
 * @date 2024/2/29
 */
@SpringBootTest
public class JmsTest {

	@Autowired
	JmsTemplate jmsTemplate;

	@Autowired
	TestService testService;


	@Test
	public void testServiceMethod() {
		testService.testMsg();
	}


}
