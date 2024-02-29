package org.redcarp.horizon.component.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * @author redcarp
 * @date 2024/2/29
 */
@Component
public class JMSService {
	@Autowired
	JmsTemplate jmsTemplate;
}
