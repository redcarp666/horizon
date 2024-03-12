package org.redcarp.horizon.security.jwt.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<String> badCredentialsExceptionHandle(BadCredentialsException exception) {
		HttpHeaders httpHeaders = new HttpHeaders();
		//设置为application-json，以防止中文乱码
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).headers(httpHeaders).body(exception.getMessage());
	}

}
