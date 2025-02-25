package org.redcarp.horizon.security.jwt.exception;

import org.redcarp.horizon.core.web.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author redcarp
 * @date 2024/2/18
 */
@RestControllerAdvice
public class SecurityExceptionHandler {

	@ExceptionHandler(value = BadCredentialsException.class)
	public Response<String> badCredentialsExceptionHandle(BadCredentialsException exception) {
		return Response.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
	}
}
