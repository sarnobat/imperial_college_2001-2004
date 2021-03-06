/*
 * Created on 05-Jun-2004
 *
 */
package exception;

 port java.sql.SQLException;

/**
 * @author ss401
 *
 */
public abstract class AbstractImportException extends SQLException {
	Exception e;

	/**
	 * @param reason
	 */
	public AbstractImportException(String reason) {
		super(reason);
	}

	/**
	 * @param e
	 */
	public AbstractImportException(SQLException e) {
		this.e = e;
	}
	
	public String toString(){
		return e.toString();
	}
}
