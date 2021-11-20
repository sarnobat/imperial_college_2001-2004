/*
 * Created on 09-Nov-2003
 *
 */
package yams.exceptions;

/**
 * @author sw00
 *
 */
public class YAMSUnsupportedInstructionException extends YAMSRuntimeException {

	/**
	 * 
	 */
	public YAMSUnsupportedInstructionException() {
		super();
	}

	/**
	 * @param message
	 */
	public YAMSUnsupportedInstructionException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public YAMSUnsupportedInstructionException(
		String message,
		Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public YAMSUnsupportedInstructionException(Throwable cause) {
		super(cause);
	}

}
