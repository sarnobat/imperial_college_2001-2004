/*
 * Created on 16-Feb-2004
 *
 */
package xer;

import org.apache.log4j.Logger;

/**
 * @author ss401
 *
 */

public class XERAttribute extends XERComponent {

	Logger logger = Logger.getLogger(this.getClass());

	boolean required;
	String name;

	/**
	 * 
	 * @param name
	 * @param nullable - Indicates whether this attribute is mandatory
	 */
	public XERAttribute(String name, int minOccurs, int maxOccurs) {
		this.name = name;
		//some redundant checks, but simply sanity checks
		if (maxOccurs == 1) {
			if (minOccurs == 0) {
				this.required = false;
			}
			else if (minOccurs == 1) {
				this.required = true;
			}
			else {
				logger.error("Inconsistent cardinailties");
			}
		}
		else {
			//TODO: This may not be erroneous, but correspond to creating a foreign key
			logger.error("XERAttribute should not be created");
		}

	}

	public String sql() {
		String sql = name;
		if (required) {
			sql += " (NOT NULL)";
		}

		return sql;
	}

}
