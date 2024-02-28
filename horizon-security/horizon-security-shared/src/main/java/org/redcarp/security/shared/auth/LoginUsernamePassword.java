package org.redcarp.security.shared.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @author redcarp
 * @date 2024/2/21
 */
public class LoginUsernamePassword extends UsernamePasswordAuthenticationToken {
	public LoginUsernamePassword(Object principal, Object credentials) {
		super(principal, credentials);
	}
}
