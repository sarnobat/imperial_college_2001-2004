/**
 * <copyright>
 * </copyright>
 * 
 * plugins/org.eclipse.xsd/src/org/eclipse/xsd/util/XSDSchemaQueryTools.java, xsd, org.eclipse.dev, 20020724_0754SL
 * @version 1.1 7/24/02
 */
package org.eclipse.xsd.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDImport;
import org.eclipse.xsd.XSDInclude;
import org.eclipse.xsd.XSDRedefine;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSchemaContent;
import org.eclipse.xsd.XSDSchemaDirective;
import org.eclipse.xsd.XSDTypeDefinition;


// Various Eclipse, etools and EMF dependencies

// xsd:annotation elements are modeled in the DOM



/**
 * XSDSchemaQueryTools is a collection of worker methods for performing 
 * simple queries or searches of schemas.
 *
 * <p>These are suitable to show examples of how to build convenience 
 * methods to more easily find components in a schema.  They also 
 * serve as useful examples of usage of the library's functions.</p>
 *
 * <p>Note that these methods are somewhat simplistic, especially 
 * in terms of support for namespaces.  To simplify the calling 
 * signatures, and to account for the 80% case where you don't have 
 * multiple namespaces, most method simply take a localName that's 
 * assumed to be correct in the current context.  However it should 
 * be simple to copy & paste these methods and add namespaceURI's to 
 * each method if desired.</p>
 *
 * @author Shane_Curcuru@us.ibm.com
 * @version $Id: XSDSchemaQueryTools.java,v 1.1 2002/05/09 21:05:36 scurcuru Exp $
 */
public abstract class XSDSchemaQueryTools
{
  /**
   * Find typeDefinitions that derive from a named type. 
   *
   * <p>This shows one way to query the schema for typeDefinitions 
   * and then how to find specific kinds of typeDefinitions.</p>
   *
   * @param schema to search for typeDefs
   * @param namespace for the type derived from
   * @param localName for the type derived from
   * @return List of any XSDTypeDefinitions found
   */
  public static List findTypesDerivedFrom(XSDSchema schema, String namespace, String localName)
  {
    if ((null == schema) || (null == localName))
    {
      throw new IllegalArgumentException("findTypesDerivedFrom called with null schema/localName");
    }

    ArrayList typesDerivedFrom = new ArrayList();

    // A handy convenience method quickly gets all 
    // typeDefinitions within our schema; note that 
    // whether or not this returns types in included, 
    // imported, or redefined schemas is subject to change
    List typedefs = schema.getTypeDefinitions();

    for (Iterator iter = typedefs.iterator(); iter.hasNext(); )
    {
      XSDTypeDefinition typedef = (XSDTypeDefinition)iter.next();
      // Walk the baseTypes from this typedef seeing if any 
      // of them match the requested one
      if (isTypeDerivedFrom(typedef, namespace, localName))
      {
        // We found it, return the original one and continue
        typesDerivedFrom.add(typedef);
        continue;
      }
    }

    return typesDerivedFrom;
  }

  /**
   * Recursive worker method to find typeDefinitions that derive 
   * from a named type. 
   *
   * <p>This is not a terribly time-efficient algorithim, but 
   * it does show usage of library methods. This works for 
   * simpleTypes; complexType support needs to be added.</p>
   *
   * @see #findTypesDerivedFrom(XSDSchema, String, String)
   * @param typeDef to see if it's derived from
   * @param namespace for the type derived from
   * @param localName for the type derived from
   * @return true if it is; false otherwise
   */
  protected static boolean isTypeDerivedFrom(XSDTypeDefinition typedef, String namespace, String localName)
  {
    // Walk the baseTypes from this typedef seeing if any 
    // of them match the requested one
    XSDTypeDefinition baseType = typedef.getBaseType();

    // As this convenience method if our parameters match
    if (baseType.hasNameAndTargetNamespace(localName, namespace))
    {
      return true;
    }
    
    // If not, check to see if we've run up to the top
    // Performance note: this is horribly inefficient, 
    // re-calling this same method every time; but it 
    // serves as a good example
    XSDTypeDefinition rootType = typedef.getRootType();
    if (rootType == baseType)
    {
      // If we've hit the root, we aren't derived from it
      return false;
    }
    else
    {
      // Otherwise continue to traverse upwards
      return isTypeDerivedFrom(baseType, namespace, localName);
    }
  }

  /**
   * Find elementDeclarations that use any types derived 
   * from a named type. 
   *
   * <p>This shows one way to query the schema for elementDeclarations 
   * and then how to find specific kinds of typeDefinitions.</p>
   *
   * @param schema to search for elemDecls
   * @param namespace for the type used
   * @param localName for the type used
   * @return List of any XSDElementDeclarations found
   */
  public static List findElementsUsingType(XSDSchema schema, String namespace, String localName)
  {
    if ((null == schema) || (null == localName))
    {
      throw new IllegalArgumentException("findElementsUsingType called with null schema/localName");
    }

    ArrayList elemsUsingType = new ArrayList();

    // A handy convenience method quickly gets all 
    // elementDeclarations within our schema; note that 
    // whether or not this returns types in included, 
    // imported, or redefined schemas is subject to change
    List elemDecls = schema.getElementDeclarations();

    for (Iterator iter = elemDecls.iterator(); 
        iter.hasNext(); /* no-op */)
    {
      XSDElementDeclaration elem = (XSDElementDeclaration)iter.next();
      XSDTypeDefinition typedef = null;
      if (elem.getAnonymousTypeDefinition() != null)
      {
        typedef = elem.getAnonymousTypeDefinition();
      }
      else if (elem.getTypeDefinition() != null)
      {
        typedef = elem.getTypeDefinition();
      }
      else
      {
        // Element is not complete, since it has no type, 
        // thus it's not using our type
        continue;
      }

      // Ask this type and walk the baseTypes from this 
      // typedef seeing if any of them match the requested one
      if (typedef.hasNameAndTargetNamespace(localName, namespace)
        || isTypeDerivedFrom(typedef, namespace, localName))
      {
        // We found it, return the element and continue
        elemsUsingType.add(elem);
        continue;
      }
    }

    return elemsUsingType;
  }

  /**
   * Find any included, imported, or redefined schemas that 
   * this one references. 
   *
   * <p>This method uses solely the Schema model API to look for 
   * XSDSchemaDirective objects. Each directive is the result 
   * of an include, import, or redefine in our schema. We then 
   * walk the tree of directives, finding their resolved schemas 
   * (if available), and produce a simple HashMap of them all.</p>
   * <p>See also {@link #hasImpInclRedef2(XSDSchema)}
   * which uses the underlying ResourceSet model to query the 
   * external resources that were loaded with this schema: it 
   * has the same effect, but using different code.</p>
   * 
   * @see #hasImpInclRedef2(XSDSchema)
   * @param schema object to search for imports/includes/redefines
   * @return HashMap where keys are the URI's of any other schemas 
   * found, and values are one of 'import', 'include', 'redefine';
   * null if none found or an error occoured
   */
  public static HashMap hasImpInclRedef(XSDSchema schema)
  {
    if (null == schema)
    {
      throw new IllegalArgumentException("hasImpInclRedef called with null schema");
    }
    HashMap hash = new HashMap();

    // Recurse to map all imports/includes/redefines
    mapImpInclRedef(schema, hash, 0);

    // If none were found, return null
    if (hash.size() == 0)
    {
      return null;
    }
    return hash;
  }

  /**
   * Find any included, imported, or redefined schemas that 
   * this one references. 
   *
   * <p>This method uses the underlying ResourceSet model to query the 
   * external resources that were loaded with this schema. 
   * Note that the ResourceSet implementation is subject to change.</p>
   * 
   * @see #hasImpInclRedef(XSDSchema)
   * @param schema object to search for imports/includes/redefines
   * @return HashMap where keys are the URI's of any other schemas 
   * found, and values are one of 'import', 'include', 'redefine';
   * null if none found or an error occoured
   */
  public static HashMap hasImpInclRedef2(XSDSchema schema)
  {
    if (null == schema)
    {
      throw new IllegalArgumentException("hasImpInclRedef2 called with null schema");
    }
    HashMap hash = new HashMap();

    // Use the Resource framework to iterate through any 
    // other resources that were loaded along with this schema 
    // to find what they import/include/redefine
    Resource currentResource = schema.eResource();

    // Get the whole set of resources that were loaded together
    ResourceSet resourceSet = currentResource.getResourceSet();

    // Iterate over all the resources, i.e., the main resource 
    // and those that have been included, imported, or redefined.
    for (Iterator resources = resourceSet.getResources().iterator(); resources.hasNext(); )
    {
      // Check for schema resources.
      Resource resource = (Resource)resources.next();
      if (resource instanceof XSDResourceImpl)
      {
        XSDResourceImpl xsdResource = (XSDResourceImpl)resource;

        // Iterate over each schema's content looking for directives.
        XSDSchema otherSchema = xsdResource.getSchema();
        for (Iterator contents = otherSchema.getContents().iterator(); contents.hasNext(); )
        {
          XSDSchemaContent content = (XSDSchemaContent)contents.next();
          if (content instanceof XSDSchemaDirective)
          {
            XSDSchemaDirective schemaDirective = (XSDSchemaDirective)content;
            String type = null;
            if (content instanceof XSDImport)
            {
              type = "XSDImport";
            }
            else if (content instanceof XSDInclude)
            {
              type = "XSDInclude";
            }
            else if (content instanceof XSDRedefine)
            {
              type = "XSDRedefine";
            }
            else
            {
              type = "unknown";
            }
            if (schemaDirective.getResolvedSchema() == null)
            {
              type += "-unresolved";
            }
            // Store the item in our hash to return to caller
            hash.put(schemaDirective.getSchemaLocation(), type);

            // Recurse down the tree into the next schema
            // using the schema API's
            XSDSchema nextSchema = schemaDirective.getResolvedSchema();
            if (null != nextSchema) 
            {
              mapImpInclRedef(nextSchema, hash, 1);
            }
          }
        }
      }
    }
    // If none were found, return null
    if (hash.size() == 0)
    {
      return null;
    }
    return hash;
  }

  /**
   * Recursive worker method implementing {@link #hasImpInclRedef(XSDSchema)}
   * and {@link #hasImpInclRedef2(XSDSchema)}. 
   *
   * <p>This uses only the schema model library API's to look for 
   * XSDSchemaDirective objects in it's content. It then checks 
   * which specific subclass of directive each is to report and 
   * then recurses to any other schemas found.</p>
   * 
   * @param schema object to search for imports/includes/redefines
   * @param HashMap where keys are the URI's of any other schemas 
   * found, and values are one of 'import', 'include', 'redefine'; 
   * this object is mutated by this method
   * @param level of iteration we're on
   */
  protected static void mapImpInclRedef(XSDSchema schema, HashMap hash, int level)
  {
    // Iterate through this schema itself to see what 
    // other schemas it *directly* import/include/redefines
    for (Iterator schemaContents = schema.getContents().iterator(); schemaContents.hasNext(); )
    {
      XSDSchemaContent schemaContent = (XSDSchemaContent)schemaContents.next();
      if (schemaContent instanceof XSDSchemaDirective)
      {
        XSDSchemaDirective schemaDirective = (XSDSchemaDirective)schemaContent;
        String type = null;
        if (schemaContent instanceof XSDImport)
        {
          type = "XSDImport-level" + level;
        }
        else if (schemaContent instanceof XSDInclude)
        {
          type = "XSDInclude-level" + level;
        }
        else if (schemaContent instanceof XSDRedefine)
        {
          type = "XSDRedefine-level" + level;
        }
        else
        {
          type = "unknown-level" + level;
        }
        if (schemaDirective.getResolvedSchema() == null)
        {
          type += "-unresolved";
        }
        // Store the item in our caller's hashtable
        hash.put(schemaDirective.getSchemaLocation(), type);

        // Recurse down the tree into the next schema
        XSDSchema nextSchema = schemaDirective.getResolvedSchema();
        if (null != nextSchema) 
        {
          mapImpInclRedef(nextSchema, hash, level + 1);
        }
      }
    }
  }
}
