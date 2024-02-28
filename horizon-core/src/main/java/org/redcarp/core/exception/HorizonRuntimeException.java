package org.redcarp.core.exception;


public class HorizonRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -2398664380961441005L;

	public HorizonRuntimeException() {
	}

	/**
	 * Use message or message format and parameters to construct a new runtime exception.
	 *
	 * @param msgOrFormat the message or message format
	 * @param args        the message format parameter
	 * @see String#format(String, Object...)
	 */
	public HorizonRuntimeException(String msgOrFormat, Object... args) {
		super(args.length == 0 ? msgOrFormat : String.format(msgOrFormat, args));
	}

	/**
	 * Use cause to construct a new runtime exception.
	 *
	 * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
	 *              (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public HorizonRuntimeException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new runtime exception with cause, suppression enabled or disabled, and writable
	 * stack trace enabled or disabled and the specified detail message or message format and
	 * parameters.
	 *
	 * @param cause              the cause. (A {@code null} value is permitted, and indicates that the cause is
	 *                           nonexistent or unknown.)
	 * @param enableSuppression  whether suppression is enabled or disabled
	 * @param writableStackTrace whether the stack trace should be writable
	 * @param msgOrFormat        the detail message or message format
	 * @param args               the detail message format parameters
	 */
	public HorizonRuntimeException(Throwable cause, boolean enableSuppression, boolean writableStackTrace, String msgOrFormat, Object... args) {
		super(args.length == 0 ? msgOrFormat : String.format(msgOrFormat, args),
		      cause,
		      enableSuppression,
		      writableStackTrace);
	}

	/**
	 * Constructs a new runtime exception with the specified detail message and cause.
	 * <p>
	 * Note: the detail message associated with cause is not automatically incorporated in this
	 * runtime exception's detail message.
	 * </p>
	 *
	 * @param cause       the cause (which is saved for later retrieval by the {@link #getCause()} method).
	 *                    (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 * @param msgOrFormat the message or message format
	 * @param args        the message format parameter
	 */
	public HorizonRuntimeException(Throwable cause, String msgOrFormat, Object... args) {
		super(args.length == 0 ? msgOrFormat : String.format(msgOrFormat, args), cause);
	}

}
