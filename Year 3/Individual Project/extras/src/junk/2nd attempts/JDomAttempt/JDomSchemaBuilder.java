/*
 * Created on 10-Feb-2004
 *
 */
import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
/**
 * @author ss401
 *
 */
public class JDomSchemaBuilder {
	
	private static Element root;
	
	public static void printSchemaInfo(String schemaPath) {
		SAXBuilder builder = new SAXBuilder();
		Document doc;
		try {
			
			doc = builder.build(new File(schemaPath));
			root = doc.getRootElement();
			System.out.println(doc.getRootElement().getName());
		} catch (Exception e) {
			System.out.println("Couldn't find schema. " + e);
		}
		List children = root.getChildren();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			Element child = (Element) iter.next();
			System.out.println(child.getName());
			if(child.getName().equals("complexType")){
				convertComplexType(child);
			}
			if(child.getName().equals("element")){
				convertElement(child);
			}
			
		}
		
	}

	private static void convertElement(Element child) {
		System.out.println("CREATE DATABASE " + child.getAttributeValue("name"));
	}

	private static void convertComplexType(Element child) {
		List children = child.getChildren();
		String sql = "CREATE TABLE " + child.getAttributeValue("name") + " (";
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			Element grandChild = (Element) iter.next();
			if(grandChild.getName().equals("attribute")){
				sql += grandChild.getAttributeValue("name") + ",";				
			}
		}
		sql = sql.substring(0,sql.length()-1) + ");";
		System.out.println(sql);
	}

}
