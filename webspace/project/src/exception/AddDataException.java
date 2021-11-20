/*
 * Created on 05-Jun-2004
 *
 */
package exception;

import java.sql. LException;

/**
 * @author ss401
 *
 */
public class AddDataException extends AbstractImportException {

	/**
	 * @param error
	 */
	public AddDataException(String error) {
		super(error);
	}

	/**
	 * @param e
	 */
	public AddDataException(SQLException e) {
		super(e);
	}



}
