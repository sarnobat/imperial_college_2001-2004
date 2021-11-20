import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDFindTypesMissingFacets;
import org.jdom.Document;
import org.jdom.input.DOMBuilder;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
 * Created on 11-Feb-2004
 *
 */

/**
 * @author ss401
 *
 */
public class XSDSchemaBuilder {

	public static void main(String[] args) {
		XSDSchema schema;
		try {
			schema =
				XSDFindTypesMissingFacets.loadSchemaUsingResourceSet(
					"schema.xsd");
			EList elements = schema.getElementDeclarations();
			for (Iterator iter = elements.iterator(); iter.hasNext();) {
				XSDElementDeclaration element =
					(XSDElementDeclaration) iter.next();
				System.out.println("Element: " + element);	
				System.out.println("Name: " + element.getName());
				System.out.println("Value: " + element.getValue());
				System.out.println("Type: " + element.getType());
				System.out.println("Type Definition:" +element.getTypeDefinition().getName());
				
				//element declaration, type definition, particle, particle content,
				NodeList children = element.getTypeDefinition().getComplexType().getContent().getElement().getChildNodes();
				System.out.println("Number of child nodes: "+ children.getLength());
				//DOMBuilder jdomBuilder = new DOMBuilder();
				//Document doc = jdomBuilder.build(children.item(1).getOwnerDocument());
				
				
				System.out.println("Content: "+element.getTypeDefinition().getComplexType().getContent().getElement().getChildNodes());
			}

		} catch (Exception e) {
			System.out.println("Couldn't load schema. " + e);
		}

	}
	
}