/*
 * Created on 18-May-2004
 *
 */
package xtractor.schemaConverter.xer.xerConstructs;

import org.apache.log4j.Logger;

import xtractor.schemaConverter.xer.XERBuilder;
import xtractor.schemaConverter.xer.XERFactory;

/**
 * @author ss401
 *
 */
public abstract class XERConstruct {

	final Logger logger = Logger.getLogger(this.getClass());

	protected String originalName;	
	protected String alias;

	XERBuilder xerBuilder;

	/**
	 * @param xerBuilder
	 */
	public XERConstruct(XERBuilder xerBuilder) {
		this.xerBuilder = xerBuilder;
	}
	
	public final String getName(){
		if(alias == null){
			return getOriginalName();
		}
		else{
			return alias;	
		}
	}
	public String getOriginalName(){
		return originalName;
	}

	/**
	 * Useful for the toString() methods.
	 * @param str - The string you wish to truncate
	 * @return - The same string as given, but if the last character is 
	 * a comma then it is removed.
	 */
	protected String removeLastComma(String str) {
		if (str.charAt(str.length() - 1) == (',')) {
			str = str.substring(0, str.length() - 1);
		}
		return str;

	}
	
	protected XERFactory getXERFactory(){
		return xerBuilder.getXERFactory();
	}
	public final String toString() {
		return getName();
	}
	
	public final void setAlias(String alias){
		this.alias = alias;
		xerBuilder.getModel().addAlias(this,alias);		
	}
	public final String getAlias(){
		return alias;
	}
	
	protected void setOriginalName(String name) {
		this.originalName = name;
	}

}
