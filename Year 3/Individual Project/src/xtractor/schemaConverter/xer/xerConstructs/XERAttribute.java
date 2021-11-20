/*
 * Created on 24-Feb-2004
 *
 */
package xtractor.schemaConverter.xer.xerConstructs;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import xtractor.schemaConverter.xer.XERBuilder;

/**
 * @author ss401
 *
 */
public class XERAttribute extends XERConstruct {

	
	private int ORIGINATION;
	public static int XSD_ATTRIBUTE = 1;
	public static int XSD_ELEMENT = 2;

	String type;

	int minOccurs;
	int maxOccurs;

	/* Constraints - required by trigger
	 * int strMinLength;
	 * int strMaxLength;
	 * int strExactLength;
	 * int numMinVal;
	 * int numMaxVal;
	 * int numTotalDigits;
	 * int numFactionDigits;
	 * Collection enumerationValues; - Note: this is null until the first value is added 
	 */
	// This map will have values of type Integer, Collection etc.
	// The keys are all Strings naming each constraint
	Map constraints;

	/**
	 * @param name
	 * @param type
	 * @param minOccurs
	 * @param maxOccurs
	 * @param xerBuilder
	 * @param origination - XSD_ATTRIBUTE or XSD_ELEMENT (accessed statically)
	 */
	public XERAttribute(String name, String type, int minOccurs, int maxOccurs, XERBuilder xerBuilder, int origination) {
		super(xerBuilder);
		//enumerationValues = new LinkedList();
		this.constraints = new HashMap();

		setProperties(name, type, minOccurs, maxOccurs);
	}

	/**
	 * Private constructor which performs the common operations of the public constructors
	 * @param name - The name of the attribute
	 * @param type - The XML Schema type of the attribte
	 * @param requirement - "optional", "required" etc. 
	 */
	protected void setProperties(String name, String type, int minOccurs, int maxOccurs) {
		setOriginalName(name);
		this.type = type;
		this.minOccurs = minOccurs;
		this.maxOccurs = maxOccurs;
	}

	/**
	 * @return - The XML Schema datatype which this attribute conforms to
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return - Indicates whether this attribute is required in the entity
	 */
	public boolean isRequired() {
		return minOccurs > 0;

	}

	private int getNumericalConstraint(String constraintName){
		return ((Integer)constraints.get(constraintName)).intValue();
	}

	/**
	 * @return
	 */
	public int getNumFactionDigits() {
		return getNumericalConstraint("numFactionDigits");
	}

	/**
	 * @return
	 */
	public int getNumMaxVal() {
		return getNumericalConstraint("numMaxVal");
	}

	/**
	 * @return
	 */
	public int getNumMinVal() {
		return getNumericalConstraint("numMinVal");
	}

	/**
	 * @return
	 */
	public int getNumTotalDigits() {
		return getNumericalConstraint("numTotalDigits");
	}

	/**
	 * @return
	 */
	public int getStrExactLength() {
		return getNumericalConstraint("strExactLength");
	}

	/**
	 * @return
	 */
	public int getStrMaxLength() {
		return getNumericalConstraint("strMaxLength");
	}

	/**
	 * @return
	 */
	public int getStrMinLength() {
		return getNumericalConstraint("strMinLength");
	}
	
	/**
	 * 
	 * @param constraintName
	 * @param constraintValue
	 */
	private void setNumericalConstraint(String constraintName,int constraintValue){
		constraints.put(constraintName,new Integer(constraintValue));
	}

	/**
	 * @param i
	 */
	public void setNumFactionDigits(int i) {		
		setNumericalConstraint("numFactionDigits",i);
	}

	/**
	 * @param i
	 */
	public void setNumMaxVal(int i) {
		setNumericalConstraint("numMaxVal",i);
	}

	/**
	 * @param i
	 */
	public void setNumMinVal(int i) {
		setNumericalConstraint("numMinVal",i);
	}

	/**
	 * @param i
	 */
	public void setNumTotalDigits(int i) {
		setNumericalConstraint("numTotalDigits",i);
	}

	/**
	 * @param i
	 */
	public void setStrExactLength(int i) {
		setNumericalConstraint("strExactLength",i);
	}

	/**
	 * @param i
	 */
	public void setStrMaxLength(int i) {
		setNumericalConstraint("strMaxLength",i);
	}

	/**
	 * @param i
	 */
	public void setStrMinLength(int i) {
		setNumericalConstraint("strMinLength",i);
	}

	public void addEnumerationValue(String s) {
		Collection enumerationValues = (Collection) this.constraints.get("enumerationValues");
		if (enumerationValues == null) {
			// No enumeration value set exists, so create one
			this.constraints.put("enumerationValues", new LinkedList());
		}
		enumerationValues.add(s);
	}

	/**
	 * We return an empty collection if this attribute is an enumeration type, but
	 * the collection stored remains null
	 * @return - A collection of Strings for each possible value of the attribute
	 */
	public Collection getEnumerationValues() {
		Collection enumerationValues = (Collection) this.constraints.get("enumerationValues");
		if (enumerationValues == null) {
			return new LinkedList();
		}
		else {
			return enumerationValues;
		}
	}

	/**
	 * @return
	 */
	public int getMinOccurs() {
		return minOccurs;
	}

	/**
	 * @return
	 */
	public int getMaxOccurs() {
		return maxOccurs;
	}

	/**
	 * This method should only be used by subclasses when cloning (in their constructor)
	 * @return - The map containing all the constraints
	 */
	protected Map getConstraints() {
		return constraints;
	}

	/**
	 * @return - True if this XER attribute originates from an XML Schema attribute
	 * (as opposed to a simple element)
	 */
	public boolean originatesFromXMLAttribute() {
		return ORIGINATION == XSD_ATTRIBUTE;
	}

	/**
	 * @return - XSD_ATTRIBUTE or XSD_ELEMENT
	 */
	public int getOrigination() {
		return ORIGINATION;
	}

}
