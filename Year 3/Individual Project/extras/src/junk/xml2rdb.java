import java.io.File;

/*import org.apache.xerces.impl.xs.XSImplementationImpl;
import org.apache.xerces.xs.*;*/
import org.jdom.Document;
import org.jdom.input.SAXBuilder;

public class xml2rdb {

	static String schemaPath = "xml/suppliers/schema.xsd.xml";

	public static void main(String args[]) {
		xerces();
	}

	public static void xerces() {

		SAXBuilder builder = new SAXBuilder();
		Document doc;
		try {
			doc = builder.build(new File(schemaPath));
			System.out.println(doc.getRootElement());
		} catch (Exception e) {
			System.out.println("Couldn't find schema. " + e);
		}

		/*XSImplementation impl = new XSImplementationImpl();
		XSLoader schemaLoader = impl.createXSLoader(null);
		XSModel model = schemaLoader.loadURI(schemaPath);
		XSNamedMap map = model.getComponents(XSConstants.TYPE_DEFINITION);

		if (map.getLength() > 0) {
			System.out.println(
				"*************************************************");
			System.out.println(
				"*************************************************");
			for (int i = 0; i < map.getLength(); i++) {
				XSObject item = map.item(i);
				System.out.println(
					"{" + item.getNamespace() + "}" + item.getName());
			}
		}*/
	}

}
