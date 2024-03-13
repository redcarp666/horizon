package org.redcarp.horizon.security.jwt.handler;

import org.redcarp.horizon.core.security.SecurityUserHolder;
import org.redcarp.horizon.security.jwt.CurrentUserHolder;
import org.springframework.stereotype.Component;

/**
 * @author redcarp
 * @date 2024/3/12
 */
@Component
public class JwtUserHolderImpl implements SecurityUserHolder {
	@Override
	public String getCurrentUserId() {
		return CurrentUserHolder.getCurrentUserId();
	}

	@Override
	public String getCurrentUserName() {
		return CurrentUserHolder.getCurrentUserName();
	}
}
