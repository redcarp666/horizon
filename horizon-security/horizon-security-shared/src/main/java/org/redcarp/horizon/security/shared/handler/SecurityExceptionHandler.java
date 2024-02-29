package org.redcarp.horizon.security.shared.handler;

import org.redcarp.common.base.domain.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 认证相关异常捕获处理器
 * @author redcarp
 * @date 2024/2/21
 */
@RestControllerAdvice
public class SecurityExceptionHandler {

	@ExceptionHandler(value = BadCredentialsException.class)
	public Response<Integer> badCredentialsExceptionHandle(BadCredentialsException exception) {
		return Response.fail(HttpStatus.UNAUTHORIZED.value(), exception.getMessage());
	}
}
