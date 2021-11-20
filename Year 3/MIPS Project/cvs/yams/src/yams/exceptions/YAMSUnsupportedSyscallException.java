/*
 * Created on 13-Nov-2003
 *
 */
package yams.exceptions;

/**
 * @author sw00
 *
 */
public class YAMSUnsupportedSyscallException extends YAMSRuntimeException {

	/**
	 * 
	 */
	public YAMSUnsupportedSyscallException() {
		super();
	}

	/**
	 * @param message
	 */
	public YAMSUnsupportedSyscallException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public YAMSUnsupportedSyscallException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public YAMSUnsupportedSyscallException(Throwable cause) {
		super(cause);
	}

}
