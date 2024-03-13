package org.redcarp.horizon.core.security;

/**
 * 系统当前用户（安全模块需要实现该接口）
 *
 * @author redcarp
 * @date 2024/3/12
 */
public interface SecurityUserHolder {
	String getCurrentUserId();

	String getCurrentUserName();

	String getCurrentToken();
}
