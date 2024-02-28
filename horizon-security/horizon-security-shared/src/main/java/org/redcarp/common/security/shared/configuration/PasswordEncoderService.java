package org.redcarp.common.security.shared.configuration;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码加密服务
 *
 * @author redcarp
 * @date 2024/2/21
 */
public class PasswordEncoderService {
	private PasswordEncoderService() {
	}

	public static PasswordEncoder getPasswordEncoder() {
		return PasswordEncoderHolder.INSTANCE;
	}

	private static class PasswordEncoderHolder {
		private static final PasswordEncoder INSTANCE = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

}
