/**
 * <copyright>
 *
 * (C) COPYRIGHT International Business Machines Corporation 2000-2002.
 *
 * </copyright>
 */
package org.eclipse.xsd.util;

// XSD library imports
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xsd.*;

/**
 * XSDFindTypesMissingFacets is a sample program showing detailed 
 * use of the common schema library.
 *
 * <p>A simple code sample.  Meant to be used with 
 * the Java Library for Schema Components.</p>
 *
 * <p>This is a standalone tool meant to be run from the command 
 * line.  Prerequsites include the library and it's associated 
 * Eclipse dependencies; you can download from eclipse.org. 
 * </p>
 *
 * <p>You will need to set up your classpath properly to run this 
 * tool; this presumes you've installed or unzipped the library 
 * and dependencies to BASEDIR below:<br/>
 * <code>
 * <br/>set ECLIPSE=BASEDIR
 * <br/>set _CP=%CLASSPATH%;%ECLIPSE%/plugins/org.eclipse.core.boot/boot.jar;%ECLIPSE%/plugins/org.eclipse.core.runtime/runtime.jar;%ECLIPSE%/plugins/org.apache.xerces/xerces.jar;%ECLIPSE%/plugins/org.eclipse.core.resources/resources.jar;%ECLIPSE%/plugins/org.eclipse.emf/emf.jar;%ECLIPSE%/plugins/org.eclipse.xsd/xsd.jar;path-or-jar-containing-XSDFindTypesMissingFacet
 * <br/>java -classpath %_CP% -DECLIPSE=%ECLIPSE% org.eclipse.xsd.util.XSDFindTypesMissingFacet [schema.xsd]
 * </code>
 * </p>
 *
 * @author Shane_Curcuru@us.ibm.com
 * @version $Id:$
 */
public abstract class XSDFindTypesMissingFacets {

	/**
	 * Use the ResourceSet to load a schema into the library.  
	 *
	 * <p>This performs initialization of various dependencies 
	 * and then asks a ResourceSet to parse and load the schema and 
	 * any dependencies into memory.  We then iterate through the 
	 * Resources in the Resource set to find the first one which 
	 * is the specific schema we asked for.  If this schema 
	 * included, imported, or redefined any other schemas, 
	 * they would be reflected as additional Resources in the 
	 * set that we could also access.</p>
	 *
	 * @param URL of a schema.xsd file
	 * @return a schema object constructed from that model; null if error
	 * @throws any underlying exceptions
	 */
	public static XSDSchema loadSchemaUsingResourceSet(String schemaURL)
		throws Exception {
		// One-time initalization of the Resource framework and 
		//  related classes; needed when running standalone
		// This is needed because we can't have the following in the plugin.xml
		//
		//  <extension point = "com.eclipse.emf.ecore.extension_parser">
		//   <parser type="xsd" class="org.eclipse.xsd.util.XSDResourceFactoryImpl"/>
		//  </extension>
		//
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
			"xsd",
			new XSDResourceFactoryImpl());

		// Create a resource set and load the main schema file into it.
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getLoadOptions().put(
			XSDResourceImpl.XSD_TRACK_LOCATION,
			Boolean.TRUE);
		XSDResourceImpl xsdSchemaResource =
			(XSDResourceImpl) resourceSet.getResource(
				URI.createURI(schemaURL),
				true);

		// getResources() returns an iterator over all the resources, i.e., the main resource
		// and those that have been included, imported, or redefined.
		for (Iterator resources = resourceSet.getResources().iterator();
			resources.hasNext();
			/* no-op */
			) {
			// Return the first schema object found
			Resource resource = (Resource) resources.next();
			if (resource instanceof XSDResourceImpl) {
				XSDResourceImpl xsdResource = (XSDResourceImpl) resource;
				return xsdResource.getSchema();
			}
		}
		System.err.println(
			"loadSchemaUsingResourceSet("
				+ schemaURL
				+ ") did not contain any schemas!");
		return null;
	}

	/**
	 * Repot any simpleTypes that are missing specific facets.  
	 *
	 * <p>We search through the provided list and find all types 
	 * that are missing either getEffectiveMaxFacet() or 
	 * getEffectiveMinFacet().  Since the library models the abstract 
	 * details of the schema, it does all the work of checking 
	 * any dependencies or parent types for any facets.</p>
	 *
	 * <p>For each offending type, we print a short report 
	 * warning the user that the type is missing some 
	 * facets.</p>
	 *
	 * @param List of XSDSimpleType definitions that we should 
	 * require to contain min/max Inclusive or Exclusive facets
	 * @param schema the types come from, used to print out 
	 * location of the schema
	 */
	public static void reportTypesMissingFacets(
		XSDSchema schema,
		List simpleTypeList) {
		// Note that this may not be set if your schema was 
		//  constructed directly from a DOM; in that case you 
		//  should probably have set it so that the library 
		//  can resolve imports, includes, and redefines
		System.out.println(
			"Schema missing max/min facet report on: "
				+ schema.getSchemaLocation());

		boolean foundMissingFacets = false;
		for (Iterator iter = simpleTypeList.iterator();
			iter.hasNext();
			/* no-op */
			) {
			XSDSimpleTypeDefinition simpleType =
				(XSDSimpleTypeDefinition) iter.next();
			// First, we can exclude any UNION or LIST types, since 
			//  the schema spec Part 2: Datatypes in:
			//  '4.1.5 Constraints on Simple Type Definition Schema Components'
			if ((XSDVariety.LIST_LITERAL == simpleType.getVariety())
				|| (XSDVariety.UNION_LITERAL == simpleType.getVariety())) {
				// Unions and lists cannot have min/max facets at all,
				//  so there's no need to report them
				continue;
			}

			// Get the effective max/min facets for each type - 
			//  this includes ones declared in this type or 
			//  that are inherited, etc.
			XSDMaxFacet maxFacet = simpleType.getEffectiveMaxFacet();
			XSDMinFacet minFacet = simpleType.getEffectiveMinFacet();

			// If we don't have the proper ones, report the error
			if ((null == maxFacet) || (null == minFacet)) {
				foundMissingFacets = true;
				if (null != simpleType.getName()) {
					// A named component's URI in the library is 
					//	effectively its <target namespace>#<name>
					System.out.println(
						"Schema named component: " + simpleType.getURI());
				} else {
					// It's an anonymous type, so ask the library 
					//  to construct an 'alias' for it
					System.out.println(
						"Schema anonymous component: "
							+ simpleType.getAliasURI());
				}
				// For an alternate (and more detailed) way to print 
				//  data about a component, please see:
				//  XSDPrototypicalSchema#printComponent(OutputStream, XSDConcreteComponent)
				//  which simply serializes the underlying concrete DOM Element 
				//  of the component to disk

				System.out.print(" is missing these required facets: ");
				if (null == maxFacet) {
					System.out.print(
						"XSDMaxFacet (either inclusive or exclusive) ");
				}
				if (null == minFacet) {
					System.out.print(
						"XSDMinFacet (either inclusive or exclusive) ");
				}
				System.out.println();
			}
		}
		if (!foundMissingFacets) {
			System.out.println(
				"OK! Schema is not missing any required facets!");
		}
	}

	/**
	 * Run a simple example from the command line.  
	 *
	 * <p>Usage: XSDFindTypesMissingFacets [schema.xsd]</p>
	 *
	 * <p>If no filename is provided, we default to 
	 * FindTypesMissingFacets.xsd</p>
	 *
	 * @param command line args; optionally a URL to a schema
	 */
	public static void main(String[] args) {
		String schemaURL = "FindTypesMissingFacets.xsd";
		if (args.length > 0) {
			// If an arg is provided, use it instead of default
			schemaURL = args[0];
		}
		try {
			// Load the schema into the library from disk
			XSDSchema schema = loadSchemaUsingResourceSet(schemaURL);

			// Check that we got a schema!
			if (null == schema) {
				System.err.println("ERROR: Could not load a XSDSchema object!");
				return;
			}

			// A handy convenience method quickly gets all 
			//  typeDefinitions within our schema
			List allTypes = schema.getTypeDefinitions();
			ArrayList allIntegerTypes = new ArrayList();
			for (Iterator iter = allTypes.iterator();
				iter.hasNext();
				/* no-op */
				) {
				XSDTypeDefinition typedef = (XSDTypeDefinition) iter.next();
				// Filter out for only simpleTypes and types 
				//  derived from the base integer type
				if ((typedef
					instanceof XSDSimpleTypeDefinition) // @see XSDSchemaQueryTools#(XSDTypeDefinition, String, String)
				// The below method quickly walks the type hierarchy 
				//  to find any types derived from integer
					&& XSDSchemaQueryTools.isTypeDerivedFrom(
						typedef,
						schema.getSchemaForSchemaNamespace(),
						"integer")) {
					// We found one, save it and continue
					allIntegerTypes.add(typedef);
				}
			}

			if (allIntegerTypes.size() < 1) {
				// Let the user know we didn't find any types to check
				System.out.println(
					"Schema: "
						+ schemaURL
						+ " did not have any integer-derived simpleTypes");
				return;
			}

			// Call our worker method to find missing facets and report
			reportTypesMissingFacets(schema, allIntegerTypes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}