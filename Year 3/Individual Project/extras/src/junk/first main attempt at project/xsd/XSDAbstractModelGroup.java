/*
 * Created on 17-Feb-2004
 *
 */
package xsd;

import org.apache.log4j.Logger;

/**
 * @author ss401
 *
 */
public abstract class XSDAbstractModelGroup {
	
	String modelGroupName;
	
	/**
	 * @param modelGroupName
	 */
	public XSDAbstractModelGroup(String modelGroupName) {
		this.modelGroupName = modelGroupName;
	}
	Logger logger = Logger.getLogger(this.getClass());
	public String toString(){
		return getModelGroupType() + " [" +getModelGroupContents()+"]";
	}
	public abstract String getModelGroupType();
	public abstract String getModelGroupContents();
	
	/**
	 * @return - The name of the table corresponding to the attribute group
	 */
	//public abstract String getModelGroupId();
	
	/**
	 * Gets the type of the element containing the model group
	 * @return
	 */
	public String getModelGroupName() {
		return modelGroupName;
	}

}
