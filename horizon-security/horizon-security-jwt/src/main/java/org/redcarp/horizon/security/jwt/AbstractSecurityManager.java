package org.redcarp.horizon.security.jwt;

import cn.hutool.extra.spring.SpringUtil;
import org.redcarp.horizon.infrastructure.utils.PreconditionUtils;
import org.redcarp.horizon.security.jwt.handler.CurrentUserHolder;
import org.redcarp.horizon.security.shared.auth.LoginUsernamePassword;
import org.redcarp.horizon.security.shared.configuration.PasswordEncoderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * @author redcarp
 * @date 2024/2/24
 */
@Slf4j
public abstract class AbstractSecurityManager implements SecurityManager, UserDetailsService {

	@Override
	public Jwt login(LoginUsernamePassword loginUsernamePassword) {
		AuthenticationManager authenticationManager = SpringUtil.getBean(AuthenticationManager.class);
		PreconditionUtils.requireNotNull(authenticationManager, "authentication.manager.is.null");
		Authentication authentication = authenticationManager.authenticate(loginUsernamePassword);
		Jwt jwt = generateJwt(authentication);
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(new JwtAuthenticationToken(jwt));
		SecurityContextHolder.setContext(context);
		loginSucceed();
		return jwt;
	}

	protected abstract Jwt generateJwt(Authentication usernamePassword);

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		AuthenticationManager authenticationManager = SpringUtil.getBean(AuthenticationManager.class);
		PreconditionUtils.requireNotNull(authenticationManager, "authentication.manager.is.null");
		String userName = CurrentUserHolder.getCurrentUserName();
		authenticationManager.authenticate(new LoginUsernamePassword(userName, oldPassword));
		log.info(String.format("Changing password for user '%s'", userName));
		updatePassword(userName, PasswordEncoderService.getPasswordEncoder().encode(newPassword));
		passwordChangeSucceed();
	}


	protected abstract void updatePassword(String userName, String newEncodedPassword);


	protected void loginSucceed() {

	}

	protected void passwordChangeSucceed() {

	}
}
