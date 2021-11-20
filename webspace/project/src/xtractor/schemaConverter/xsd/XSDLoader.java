/*
 * Created on 24-Feb-2004
 *
 */
package xtractor.schemaConverter.xsd;

import java.io.File;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;
import org.eclipse.xsd.util.XSDResourceImpl;
/**
 * @author ss401
 *
 */
public class XSDLoader {
	static Logger logger = Logger.getLogger("XSDLoader");

	/**
	 * @param schemaFile - The xsd file
	 * @return - An XSD Infomodel representation of the specified schema
	 */
	public static XSDSchema loadXSDInfoModel(File schemaFile) {

		//For more information, see XSDFindTypesMissingFacets.java
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xsd", new XSDResourceFactoryImpl());

		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getLoadOptions().put(XSDResourceImpl.XSD_TRACK_LOCATION, Boolean.TRUE);
		//XSDResourceImpl xsdSchemaResource = (XSDResourceImpl) resourceSet.getResource(URI.createURI(schemaFile.getAbsolutePath()), true);
		XSDResourceImpl xsdSchemaResource = (XSDResourceImpl) resourceSet.getResource(URI.createFileURI(schemaFile.getAbsolutePath()), true);

		for (Iterator resources = resourceSet.getResources().iterator(); resources.hasNext();) {
			Resource resource = (Resource) resources.next();
			if (resource instanceof XSDResourceImpl) {
				XSDResourceImpl xsdResource = (XSDResourceImpl) resource;
				return xsdResource.getSchema();
			}
		}
		logger.error("loadSchemaUsingResourceSet(" + schemaFile.getName() + ") did not contain any schemas.");
		return null;
	}

}
