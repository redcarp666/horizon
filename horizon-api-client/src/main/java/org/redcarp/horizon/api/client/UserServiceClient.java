package org.redcarp.horizon.api.client;

import org.redcarp.horizon.api.client.dto.ClientRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author redcarp
 * @date 2024/3/13
 */
@FeignClient(name = "SYSTEM-SERVICE")
public interface UserServiceClient {
	@RequestMapping(method = RequestMethod.POST, value = "/system/sysUser/getList")
	Object getUserList(@RequestBody ClientRequestDto params);

}

