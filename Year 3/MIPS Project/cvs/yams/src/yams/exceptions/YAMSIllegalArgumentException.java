/*
 * Created on 09-Nov-2003
 *
 */
package yams.exceptions;

/**
 * @author sw00
 *
 */
public class YAMSIllegalArgumentException extends YAMSRuntimeException {


	public YAMSIllegalArgumentException() {
		super();
	}

	/**
	 * @param message
	 */
	public YAMSIllegalArgumentException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public YAMSIllegalArgumentException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public YAMSIllegalArgumentException(Throwable cause) {
		super(cause);
	}

}
