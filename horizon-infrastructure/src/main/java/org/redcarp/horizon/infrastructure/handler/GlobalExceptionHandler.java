package org.redcarp.horizon.infrastructure.handler;

import org.redcarp.horizon.infrastructure.domain.Response;
import org.redcarp.horizon.infrastructure.exception.HorizonBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
	public Response<Integer> InspurBusinessExceptionHandle(HorizonBusinessException exception) {
		String messageSourceMessage = messageSource.getMessage(exception.getMessageKey(),
		                                                       exception.getParams(),
		                                                       "something wrong",
		                                                       LocaleContextHolder.getLocale());
		return Response.fail(Optional.ofNullable(exception.getCode()).orElse(FAIL).intValue(), messageSourceMessage);
	}
}
