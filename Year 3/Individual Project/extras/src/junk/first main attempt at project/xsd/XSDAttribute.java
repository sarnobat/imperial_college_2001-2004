/*
 * Created on 17-Feb-2004
 *
 */
package xsd;

import org.jdom.Element;

/**
 * @author ss401
 *
 */
public class XSDAttribute {

	String name;
	int minOccurs;
	int maxOccurs;
	/**
	 * If minOccurs or maxOccurs are null, it means the schema hasn't specified them and they
	 * default to 1 
	 */
	//public XSDAttribute(String attributeName, String minOccurs, String maxOccurs) {
	public XSDAttribute(Element schemaComponent) {
		this.name = schemaComponent.getAttributeValue("name");
		this.minOccurs = resolveCardinality(schemaComponent.getAttributeValue("minOccurs"));
		this.maxOccurs = resolveCardinality(schemaComponent.getAttributeValue("maxOccurs"));
	}
	public XSDAttribute(String name,int minOccurs,int maxOccurs) {
			this.name = name;
			this.minOccurs = minOccurs;
			this.maxOccurs = maxOccurs;
	}
	private int resolveCardinality(String number) {
		//TODO: The value of min/maxOccurs could be "union" among others. Currently this hasn't been dealt with
		if (number == null) {
			return 1;
		}
		else if(number.equals("unbounded")){
			return 9999;
		}
		else {
			return Integer.parseInt(number);
		}

	}
	/**
	 * Convenience Method
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * Convenience Method
	 * @return
	 */
	public int getMaxOccurs() {
		return this.maxOccurs;
	}
	/**
	 * Convenience Method
	 * @return
	 */
	public int getMinOccurs() {
		return this.minOccurs;
	}

}
