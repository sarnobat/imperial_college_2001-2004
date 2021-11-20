package xsd;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDTypeDefinition;

/*
 * Created on 16-Feb-2004
 *
 */

/**
 * @author ss401
 *
 */

public class XSDModelInvestigator extends XSDSchemaManipulator{
	Logger logger = Logger.getLogger(this.getClass());
	
	public XSDModelInvestigator(String schemaURL) {
		super(schemaURL);
		printAllElements();
		printAllTypes();
	}

	private void printAllTypes(){
		EList typeDefs = schema.getTypeDefinitions();
		logger.debug("\nNumber of type definitions: " +typeDefs.size());
		for(int i = 0;i<typeDefs.size();i++){
			XSDTypeDefinition typeDef = (XSDTypeDefinition) typeDefs.get(i);
			logger.debug(typeDef.getName() +"(complex = " +isComplexType(typeDef.getName())+")");
		}
	}
	private void printAllElements(){
		logger.debug("\nAnother Element: "+schema.getElement());
		EList elemDefs = schema.getElementDeclarations();
		logger.debug("Number of element definitions: " +elemDefs.size());
		for(int i = 0;i<elemDefs.size();i++){
			XSDElementDeclaration elemDef = (XSDElementDeclaration) elemDefs.get(i);
			logger.debug(elemDef.getName());
		}
	}
	

}
