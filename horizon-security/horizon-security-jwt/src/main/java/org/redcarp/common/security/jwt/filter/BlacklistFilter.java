package org.redcarp.common.security.jwt.filter;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.redcarp.common.security.jwt.exception.JwtTokenBlackException;
import org.redcarp.common.security.jwt.handler.BlacklistHandler;
import org.redcarp.common.security.jwt.handler.CurrentUserHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;

/**
 * 黑名单检查
 *
 * @author redcarp
 * @date 2024/2/26
 */
public class BlacklistFilter extends OncePerRequestFilter {


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		Jwt jwt = CurrentUserHolder.getCurrentJwt();
		try {
			if (jwt != null) {
				Instant issuedAt = jwt.getIssuedAt();
				String userId = jwt.getSubject();
				BlacklistHandler blacklistHandler = SpringUtil.getBean(BlacklistHandler.class);
				String blackTime = blacklistHandler.getBlackTime(userId);
				if (issuedAt != null) {
					if (blackTime != null) {
						if (DateUtil.parse(blackTime).isAfter(Date.from(issuedAt))) {
							throw new JwtTokenBlackException("请重新登录！");
						}
					}
				}
			}
			filterChain.doFilter(request, response);
		} catch (JwtTokenBlackException exception) {
			response.sendError(HttpStatus.FORBIDDEN.value(), exception.getMessage());
		}
	}
}
