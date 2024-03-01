package org.redcarp.horizon.component.jms.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;

import java.io.Serializable;

/**
 * @author redcarp
 * @date 2024/2/29
 */
@SpringBootTest
public class JmsTest {

	@Autowired
	JmsTemplate jmsTemplate;

	@Test
	public void produce() {
		JmsMessageInput input = new JmsMessageInput(1, "message");
		jmsTemplate.convertAndSend("destination_1", input);
	}

	@JmsListener(destination = "destination_1")
	public void consume1(JmsMessageInput msg) {
		System.out.println("consume1 = " + msg.toString());
	}

	@JmsListener(destination = "destination_1")
	public void consume2(JmsMessageInput msg) {
		System.out.println("consume2 = " + msg.toString());
	}


	public static class JmsMessageInput implements Serializable {
		private Integer id;
		private String msg;

		public JmsMessageInput(Integer i, String message) {
			this.id = i;
			this.msg = message;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		@Override
		public String toString() {
			return "JmsMessageInput{" + "id=" + id + ", msg='" + msg + '\'' + '}';
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
	}
}
