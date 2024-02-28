package org.redcarp.common.security.jwt;

import org.redcarp.common.security.shared.auth.LoginUsernamePassword;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * 安全管理器
 *
 * @author redcarp
 * @date 2024/2/24
 */
public interface SecurityManager {
	/**
	 * 登录成功后生成jwt
	 */
	Jwt login(LoginUsernamePassword loginUsernamePassword);

	/**
	 * 修改密码
	 *
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * @author redcarp
	 * @date 2024/2/26
	 */
	void changePassword(String oldPassword, String newPassword);
}
