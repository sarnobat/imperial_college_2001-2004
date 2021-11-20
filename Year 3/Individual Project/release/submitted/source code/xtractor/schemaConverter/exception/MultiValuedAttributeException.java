/*
 * Created on 20-May-2004
 *
 */
package xtractor.schemaConverter.exception;

/**
 * @author ss401
 *
 */
public class MultiValuedAttributeException extends Exception {
	
	String createForeignTableSQL;
	
	/**
	 * 
	 */
	public MultiValuedAttributeException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MultiValuedAttributeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public MultiValuedAttributeException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param createForeignTableSQL - The SQL to create the foreign table to 
	 * hold the multi-values of the parent's table. Note it will contain a
	 * foreign key back to the parent table
	 */
	public MultiValuedAttributeException(String createForeignTableSQL) {
		this.createForeignTableSQL = createForeignTableSQL;
	}

	/**
	 * @return - The SQL to create the foreign table holding the multi-values
	 */
	public String getCreateForeignTableSQL() {
		return this.createForeignTableSQL;
	}

}
