/*
 * Created on 17-Feb-2004
 *
 */
package xsd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * @author ss401
 *
 */
public class XSDSequence extends XSDAbstractModelGroup {

	String sequenceId;
	//Map from element type (element, attribute, complexType etc.) represented as a String to a jDom element
	Map childElements;
	Logger logger = Logger.getLogger(this.getClass());
	/**
	 * @param sequenceChildren - A list of jdom elements (element, attribute, choice, group etc.) within
	 * an modelGroup structure
	 */
	public XSDSequence(String modelGroupName,List content) {
		super(modelGroupName);
		/*childNames = new ArrayList();
		for (Iterator iter = content.iterator(); iter.hasNext();) {
			Element child = (Element) iter.next();
			this.childNames.add(child.getAttributeValue("name"));
			logger.debug("Sequence child \""+child.getAttributeValue("name")+"\" added");
		}*/
		childElements = new TreeMap();
		for (Iterator iter = content.iterator(); iter.hasNext();) {
			Element child = (Element) iter.next();
			this.childElements.put(child.getAttributeValue("name"), child);
			logger.debug("Sequence child \"" + child.getAttributeValue("name") + "\" added");
		}
		sequenceId = "sequence_" + GlobalUtilities.getNextSequenceId();
	}
	/*public void addChildName(String name) {
		childNames.add(name);
	}*/

	/**
	 * @return - A list of names of elements that are children of this modelGroup element
	 */
	/*public List getChildNames() {
		return childNames;
	}*/
	/**
	 * @return - A list of JDom elements that are children of this modelGroup element
	 */
	public List getChildrenElements() {
		return new ArrayList(childElements.values());
	}

	public String toString() {
		return super.toString();
	}
	/* (non-Javadoc)
	 * @see xsd.XSDAbstractModelGroup#getModelGroupName()
	 */
	public String getModelGroupType() {
		return "SEQUENCE";
	}
	/* (non-Javadoc)
	 * @see xsd.XSDAbstractModelGroup#getModelGroupContents()
	 */
	public String getModelGroupContents() {
		String childNameList = "";
		for (Iterator iter = childElements.keySet().iterator(); iter.hasNext();) {
			String childName = (String) iter.next();
			childNameList += childName+",";
		}
		return GlobalUtilities.removeLastColon(childNameList);
	}
	/* (non-Javadoc)
	 * @see xsd.XSDAbstractModelGroup#getModelGroupId()
	 */
	public String getModelGroupId() {
		return sequenceId;
	}

}
