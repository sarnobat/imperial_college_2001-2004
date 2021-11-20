/*
 * Created on 26-May-2004
 *
 */
package xtractor.schemaConverter.exception;

/**
 * @author ss401
 *
 */
public class ForeignTableException extends Exception {

	String parentDataTable;
	String foregnDataAndMetaTables;

	/**
	 * @param sql
	 * @param foreignTablesSQL
	 */
	public ForeignTableException(String sql, String foreignTablesSQL) {
		this.parentDataTable = sql;
		this.foregnDataAndMetaTables = foreignTablesSQL;
	}

	/**
	 * @return
	 */
	public String getForegnDataAndMetaTables() {
		return foregnDataAndMetaTables;
	}

	/**
	 * @return
	 */
	public String getParentDataTable() {
		return parentDataTable;
	}

}
