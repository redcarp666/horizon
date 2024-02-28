package org.redcarp.security.jwt.handler;

import org.redcarp.core.exception.NotSupportedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * 当前用户
 *
 * @author redcarp
 * @date 2024/2/26
 */
public class CurrentUserHolder {
	public static Authentication getCurrentAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public static Jwt getCurrentJwt() {
		Authentication authentication = getCurrentAuthentication();
		if (authentication instanceof JwtAuthenticationToken) {
			return ((JwtAuthenticationToken) authentication).getToken();
		} else {
			return null;
		}
	}

	public static String getCurrentUserId() {
		Authentication authentication = getCurrentAuthentication();
		if (authentication instanceof JwtAuthenticationToken) {
			return authentication.getName();
		} else {
			throw new NotSupportedException();
		}
	}

	public static String getCurrentUserName() {
		Authentication authentication = getCurrentAuthentication();
		if (authentication instanceof JwtAuthenticationToken) {
			Jwt token = ((JwtAuthenticationToken) authentication).getToken();
			return token.getClaimAsString("name");
		} else {
			return authentication.getPrincipal().toString();
		}
	}

}
