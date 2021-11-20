/*
 * Created on 10-Feb-2004
 *  
 */

import java.util.LinkedList;
import java.util.List;

import org.apache.xerces.impl.xs.XSImplementationImpl;
import org.apache.xerces.xs.*;
/**
 * @author ss401
 *  
 */
public class XercesSchemaBuilder {
	public static void printSchemaInfo(String schemaPath) {

		XSImplementation impl = new XSImplementationImpl();
		XSLoader schemaLoader = impl.createXSLoader(null);
		XSModel model = schemaLoader.loadURI(schemaPath);
		
		XSNamedMap map = model.getComponentsByNamespace(XSConstants.ELEMENT_DECLARATION,"http://www.suppliers.com");
		System.out.println(map.getLength());
		
		XSObject obj0 = map.item(0);
				
		System.out.println(obj0.getName());
		
		((XSElementDeclaration) obj0).getEnclosingCTDefinition();
		
		
		
		/*XSImplementation impl = new XSImplementationImpl();
		XSLoader schemaLoader = impl.createXSLoader(null);
		XSModel model = schemaLoader.loadURI(schemaPath);

		System.out.println("Root Element Name");
		System.out.println("--------------------------------");
		System.out.println(getRootElementName(model));
		System.out.println();
		System.out.println("Top Level Types");
		System.out.println("--------------------------");
		String[] topLevelTypes = getTopLevelTypes(model);
		for (int i = 0; i < topLevelTypes.length; i++) {
			System.out.println(topLevelTypes[i]);
		}		
		System.out.println();*/
		/*short[] components = { XSConstants.ELEMENT_DECLARATION,XSConstants.ATTRIBUTE_DECLARATION,XSConstants.ATTRIBUTE_GROUP,XSConstants.ATTRIBUTE_USE,XSConstants.TYPE_DEFINITION };
		for (int j = 0; j < components.length; j++) {

			XSNamedMap map = model.getComponents(components[j]);

			if (map!= null && map.getLength() > 0) {
				System.out.println("*************************************************");
				System.out.println("*************************************************");
				for (int i = 0; i < map.getLength(); i++) {
					XSObject item = map.item(i);
					System.out.println("{" + item.getNamespace() + "}" + item.getName());
				}
			}
		}*/

		/*
		 * StringList namespaces = model.getNamespaces();
		 * for (int i = 0; i
		 * < namespaces.getLength(); i++) {
		 * System.out.println(namespaces.item(i));
		 */

	}
	
	private static String getRootElementName(XSModel schema){
		//TODO:Not sure what to do when there is more than 1 top level element declaration
		return schema.getComponents(XSConstants.ELEMENT_DECLARATION).item(0).getName();
	}
	/**
	 * This should ignore the XML Namespace
	 * It should also omit the root element
	 * @param schema
	 * @return
	 */
	private static String[] getTopLevelTypes(XSModel schema){
		XSNamedMap m = schema.getComponentsByNamespace(XSConstants.TYPE_DEFINITION,getDefaultNamespace(schema));
		//String[] types = new String[m.getLength()];
		List types = new LinkedList();
		for (int i = 0; i < m.getLength(); i++) {
			String type = m.item(i).getName();
			String root = getRootElementName(schema); 
			if(!type.equals(root)){
				types.add(type);
			}
		}
		String[] typesArr = new String[types.size()];
		for (int i = 0; i < types.size(); i++) {
			typesArr[i] = (String) types.get(i);			
		}
		return typesArr;
	}
	private static String[] stringListToArray(StringList l){
		String[] array = new String[l.getLength()];
		for (int i = 0; i < l.getLength(); i++) {
			array[i] = l.item(0);			
		}
		return array;
	}
	
	//TODO:This is a kludge
	private static String getDefaultNamespace(XSModel schema){
		String[] namespaces = stringListToArray(schema.getNamespaces());
		if(namespaces.length > 2){
			System.out.println("Warning: more than 2 namespaces.");
		}
		for (int i = 0; i < namespaces.length; i++) {
			if(namespaces[i] == "http://www.w3.org/2001/XMLSchema" ){
				continue;
			}
			return namespaces[i];
		}
		return null;
	}
}
