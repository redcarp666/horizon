package org.redcarp.horizon.security.jwt;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.log4j.Log4j2;
import org.redcarp.horizon.core.util.AssertionUtils;
import org.redcarp.horizon.security.jwt.auth.LoginUsernamePassword;
import org.redcarp.horizon.security.jwt.config.PasswordEncoderService;
import org.redcarp.horizon.security.jwt.handler.CurrentUserHolder;
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
@Log4j2
public abstract class AbstractSecurityManager implements SecurityManager, UserDetailsService {

	@Override
	public Jwt login(LoginUsernamePassword loginUsernamePassword) {
		AuthenticationManager authenticationManager = SpringUtil.getBean(AuthenticationManager.class);
		AssertionUtils.shouldNotNull(authenticationManager, "authentication.manager.is.null");
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
		AssertionUtils.shouldNotNull(authenticationManager, "authentication.manager.is.null");
		String userName = CurrentUserHolder.getCurrentUserName();
		authenticationManager.authenticate(new LoginUsernamePassword(userName, oldPassword));
		log.info(String.format("Changing password for user '%s'", userName));
		updatePassword(userName, PasswordEncoderService.getPasswordEncoder().encode(newPassword));
		logout();
	}


	protected abstract void updatePassword(String userName, String newEncodedPassword);


	protected void loginSucceed() {

	}

	public abstract void logout();

}
