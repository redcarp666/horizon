package org.redcarp.horizon.core.exception;


public class NotSupportedException extends HorizonRuntimeException {

	private static final long serialVersionUID = 6106399720806482079L;

	public NotSupportedException() {
	}

	public NotSupportedException(String msgOrFormat, Object... args) {
		super(msgOrFormat, args);
	}

}
