package org.redcarp.horizon.infrastructure.interceptor;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

/**
 * 国际化拦截器
 *
 * @author redcarp
 * @date 2024/2/19
 */
public class LocalizationInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String acceptLanguage = request.getHeader("Accept-Language");
		if (Objects.nonNull(acceptLanguage)) {
			Locale locale = Locale.lookup(Locale.LanguageRange.parse(acceptLanguage),
			                              Arrays.asList(Locale.getAvailableLocales()));
			LocaleContextHolder.setLocale(locale);
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		LocaleContextHolder.resetLocaleContext();
	}
}
