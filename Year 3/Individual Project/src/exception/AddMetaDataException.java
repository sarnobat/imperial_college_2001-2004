/*
 * Created on 05-Jun-2004
 *
 */
package exception;

import j a.sql.SQLException;

/**
 * @author ss401
 *
 */
public class AddMetaDataException extends AbstractImportException {

	Exception e;

	/**
	 * @param e
	 */
	public AddMetaDataException(SQLException e) {
		super(e);
	}

}
