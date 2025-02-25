package org.redcarp.horizon.infrastructure.exception;

import lombok.Getter;
import org.redcarp.horizon.core.exception.HorizonRuntimeException;

/**
 * 业务异常类
 *
 * @author redcarp
 * @date 2024/2/18
 */
@Getter
public class HorizonBusinessException extends HorizonRuntimeException {
	private final String messageKey;
	private Integer code;
	private Object[] params;

	public HorizonBusinessException(String messageKey, String... params) {
		super(messageKey);
		this.params = params;
		this.messageKey = messageKey;
	}

	public HorizonBusinessException(int code, String messageKey, String... params) {
		super(messageKey);
		this.messageKey = messageKey;
		this.code = code;
		this.params = params;
	}

	public HorizonBusinessException(String detailMessage, String messageKey, int code, String[] params, Object... detailMessageArgs) {
		super(detailMessage, detailMessageArgs);
		this.messageKey = messageKey;
		this.code = code;
		this.params = params;
	}

	public HorizonBusinessException(String messageKey, String detailMessage, Object... detailMessageArgs) {
		super(detailMessage, detailMessageArgs);
		this.messageKey = messageKey;
	}

}
