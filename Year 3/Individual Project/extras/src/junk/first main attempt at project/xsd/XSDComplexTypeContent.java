package xsd;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jdom.Element;

/*
 * Created on 16-Feb-2004
 *
 */

/**
 * @author ss401
 * This class is only used as an intermediate before generating an XER model, because 
 * there is too much information to encode conveniently in a simple data structure. No flattening etc. takes place here.
 */

public class XSDComplexTypeContent {

	XSDAbstractModelGroup modelGroup;

	Map attributes = new TreeMap(); 


	/**
	 * @param complexTypeName
	 * @param xsdWalker - Entire schema manipulator is passed here so the schema can be accessed directly
	 * (XSDSchemaManipulator class would become too crowded if constructed there)
	 */
	public XSDComplexTypeContent(String complexTypeName, XSDSchemaManipulator xsdWalker) {
		Element complexTypeElem = xsdWalker.getTypeDefinitionElement(complexTypeName);
		List children = complexTypeElem.getChildren();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			Element child = (Element) iter.next();
			List grandChildNames = getNames(child.getChildren());

			if (child.getName().equals("sequence")) {
				modelGroup = new XSDSequence(complexTypeName,child.getChildren());

			}
			else if (child.getName().equals("group")) {
				modelGroup = new XSDGroup(complexTypeName,child.getChildren());
			}
			else if (child.getName().equals("all")) {
				modelGroup = new XSDAll(complexTypeName,child.getChildren());
			}
			else if (child.getName().equals("choice")) {
				modelGroup = new XSDChoice(complexTypeName,child.getChildren());
			}
			else if (child.getName().equals("attribute")) {
				XSDAttribute xsdAttribute = new XSDAttribute(child);
				attributes.put(child.getAttributeValue("name"),xsdAttribute);				
			}
			else if (child.getName().equals("attributeGroup")) {
				//TODO Handle attributeGroup;
			}
			else if (child.getName().equals("anyAttribute")) {
				//TODO Handle anyAttribute;
			}
		}
	}

	/**
	 * @param list - A List of JDom Elements
	 * @return - A list of Strings for the name of each element
	 */
	private List getNames(List elements) {
		List l = new ArrayList();
		for (Iterator iter = elements.iterator(); iter.hasNext();) {
			Element element = (Element) iter.next();
			l.add(element.getName());
		}
		return l;
	}

	/*public void addSequenceElement(String elementName) {
		modelGroup.addChildName(elementName);
	}*/

	/*public void addAttribute(String attributeName) {
		attributes.add(attributeName);
	}*/

	public String toString() {
		String buf = modelGroup.toString();
		/*List sequenceElements = modelGroup.getChildNames();
		for (Iterator iter = sequenceElements.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			buf += element + ",";
		}
		buf += "} ";*/
		buf += " ATTRIBUTES {";
		for (Iterator iter = attributes.keySet().iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			buf += element + ",";
		}
		buf = GlobalUtilities.removeLastColon(buf);
		buf += "} ]";
		return buf;
	}

	/**
	 * @return A List of Strings for each element, attribute etc. contained within this complex type
	 */
	//	public List getNames() {
	//		List l = new ArrayList(sequenceElements);
	//		l.addAll(attributes);
	//		return l;
	//	}

	/**
	 * @return - The XSD Sequence object for the modelGroup element contained in this complex type
	 */
	public XSDSequence getModelGroup() {
		return (XSDSequence) modelGroup;
	}
	/**
	 * @return - A list of Strings, each the name of an attribute
	 */
	public List getAttributeNames() {
		return new ArrayList(attributes.keySet());
	}
	/**
	 * 
	 * @return - A list of XSDAttributes in this complex type
	 */
	public List getAttributes(){
		return new ArrayList(attributes.values());
	}

}
