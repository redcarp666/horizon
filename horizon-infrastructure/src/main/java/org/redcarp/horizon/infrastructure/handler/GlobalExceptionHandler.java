package org.redcarp.horizon.infrastructure.handler;

import org.apache.commons.lang3.StringUtils;
import org.redcarp.horizon.infrastructure.domain.Response;
import org.redcarp.horizon.infrastructure.exception.HorizonBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

import static org.redcarp.horizon.infrastructure.domain.Response.FAIL;

/**
 * 全局异常处理器
 *
 * @author redcarp
 * @date 2024/2/18
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	@ExceptionHandler(value = HorizonBusinessException.class)
	public Response<Integer> horizonBusinessExceptionHandle(HorizonBusinessException exception) {
		String messageSourceMessage = messageSource.getMessage(exception.getMessageKey(),
		                                                       exception.getParams(),
		                                                       exception.getMessageKey(),
		                                                       LocaleContextHolder.getLocale());
		return Response.fail(Optional.ofNullable(exception.getCode()).orElse(FAIL).intValue(), messageSourceMessage);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public Response<String> methodArgumentNotValidExceptionHandle(MethodArgumentNotValidException exception) {
		FieldError fieldError = exception.getFieldError();
		if (fieldError != null) {
			String defaultMessage = fieldError.getDefaultMessage();
			if (StringUtils.isNotBlank(defaultMessage)) {
				String messageSourceMessage = messageSource.getMessage(defaultMessage,
				                                                       null,
				                                                       defaultMessage,
				                                                       LocaleContextHolder.getLocale());
				return Response.restResult(fieldError.getField(), HttpStatus.BAD_REQUEST.value(), messageSourceMessage);
			}
		}
		return Response.fail();
	}
}
