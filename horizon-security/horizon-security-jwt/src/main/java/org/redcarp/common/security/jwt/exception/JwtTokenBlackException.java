package org.redcarp.common.security.jwt.exception;

import org.redcarp.core.exception.HorizonRuntimeException;

/**
 * @author redcarp
 * @date 2024/2/26
 */
public class JwtTokenBlackException extends HorizonRuntimeException {

	public JwtTokenBlackException(String msgOrFormat, Object... args) {
		super(msgOrFormat, args);
	}
}
