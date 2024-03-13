package org.redcarp.horizon.api.client.test;

import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.Test;
import org.redcarp.horizon.api.client.ClientRequestDto;
import org.redcarp.horizon.api.client.UserServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author redcarp
 * @date 2024/3/13
 */
@SpringBootTest
public class ApiTest {
	@Autowired
	UserServiceClient client;

	@Test
	public void testApiClient() {
		ClientRequestDto<Object> requestDto = new ClientRequestDto<>();
		ClientRequestDto.Page page = new ClientRequestDto.Page();
		page.setSize(1);
		requestDto.setPage(page);
		//spring-boot-starter-json 需要添加该依赖，才有requestBody请求的转换器
		Object userList = client.getUserList(requestDto);
		String jsonString = JSON.toJSONString(userList);
		System.out.println("jsonString = " + jsonString);
	}
}
