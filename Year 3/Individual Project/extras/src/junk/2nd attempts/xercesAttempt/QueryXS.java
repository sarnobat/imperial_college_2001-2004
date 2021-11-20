import org.apache.xerces.dom3.DOMConfiguration;
import org.apache.xerces.dom3.DOMError;
import org.apache.xerces.dom3.DOMErrorHandler;
import org.apache.xerces.dom3.bootstrap.DOMImplementationRegistry;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSImplementation;
import org.apache.xerces.xs.XSLoader;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObject;
import org.w3c.dom.ls.LSParser;

/**
 * This sample program illustrates how to use load XML Schemas and use XML
 * Schema API (org.apache.xerces.xs) to navigate XML Schema components.
 * 
 * @author Elena Litani, IBM
 * @version $Id: QueryXS.java,v 1.4 2003/11/19 00:07:01 elena Exp $
 */
public class QueryXS implements DOMErrorHandler {

	/** Default namespaces support (true). */
	protected static final boolean DEFAULT_NAMESPACES = true;

	/** Default validation support (false). */
	protected static final boolean DEFAULT_VALIDATION = false;

	/** Default Schema validation support (false). */
	protected static final boolean DEFAULT_SCHEMA_VALIDATION = false;

	static LSParser builder;
	public static void main(String[] argv) {

		if (argv.length == 0) {
			printUsage();
			System.exit(1);
		}

		try {
			// get DOM Implementation using
			// DOM Registry
			System.setProperty(DOMImplementationRegistry.PROPERTY, "org.apache.xerces.dom.DOMXSImplementationSourceImpl");
			DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();

			XSImplementation impl = (XSImplementation) registry.getDOMImplementation("XS-Loader");

			XSLoader schemaLoader = impl.createXSLoader(null);

			DOMConfiguration config = schemaLoader.getConfig();

			// create Error Handler
			DOMErrorHandler errorHandler = new QueryXS();

			// set error handler
			config.setParameter("error-handler", errorHandler);

			// set validation feature
			config.setParameter("validate", Boolean.TRUE);

			// parse document
			System.out.println("Parsing " + argv[0] + "...");
			XSModel model = schemaLoader.loadURI("schema.xsd"); //argv[0]);
			if (model != null) {
				// element
				// declarations
				XSNamedMap map = model.getComponents(XSConstants.ELEMENT_DECLARATION);
				if (map.getLength() != 0) {
					System.out.println("\n*************************************************");
					System.out.println("* Global element declarations: {namespace} name ");
					System.out.println("*************************************************");
					for (int i = 0; i < map.getLength(); i++) {
						XSObject item = map.item(i);
						System.out.println("{" + item.getNamespace() + "}" + item.getName());
					}
				}
				// attribute
				// declarations
				map = model.getComponents(XSConstants.ATTRIBUTE_DECLARATION);
				if (map.getLength() != 0) {
					System.out.println("\n*************************************************");
					System.out.println("* Global attribute declarations: {namespace} name");
					System.out.println("*************************************************");
					for (int i = 0; i < map.getLength(); i++) {
						XSObject item = map.item(i);
						System.out.println("{" + item.getNamespace() + "}" + item.getName());
					}
				}
				// notation
				// declarations

				map = model.getComponents(XSConstants.TYPE_DEFINITION);
				if (map.getLength() != 0) {
					System.out.println("\n*************************************************");
					System.out.println("* Global type declarations: {namespace} name");
					System.out.println("*************************************************");
					for (int i = 0; i < map.getLength(); i++) {
						XSObject item = map.item(i);
						System.out.println("{" + item.getNamespace() + "}" + item.getName());
					}
				}

				// notation
				// declarations
				map = model.getComponents(XSConstants.NOTATION_DECLARATION);
				if (map.getLength() != 0) {
					System.out.println("\n*************************************************");
					System.out.println("* Global notation declarations: {namespace} name");
					System.out.println("*************************************************");
					for (int i = 0; i < map.getLength(); i++) {
						XSObject item = map.item(i);
						System.out.println("{" + item.getNamespace() + "}" + item.getName());
					}
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void printUsage() {

		System.err.println("usage: java dom.QueryXS uri ...");
		System.err.println();

	} // printUsage()

	public boolean handleError(DOMError error) {
		short severity = error.getSeverity();
		if (severity == DOMError.SEVERITY_ERROR) {
			System.out.println("[xs-error]: " + error.getMessage());
		}

		if (severity == DOMError.SEVERITY_WARNING) {
			System.out.println("[xs-warning]: " + error.getMessage());
		}
		return true;

	}

}
