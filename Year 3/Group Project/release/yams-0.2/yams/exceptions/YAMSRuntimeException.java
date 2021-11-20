/*
 * Created on 09-Nov-2003
 *
 */
package yams.exceptions;

/**
 * @author sw00
 *
 */
public class YAMSRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	public YAMSRuntimeException() {
		super();
	}

	/**
	 * @param message
	 */
	public YAMSRuntimeException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public YAMSRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public YAMSRuntimeException(Throwable cause) {
		super(cause);
	}

}
