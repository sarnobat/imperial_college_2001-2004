/*
 * Created on 08-Mar-2004
 *
 */
package xtractor.schemaConverter.xer;
import java.util.Collection;

import org.apache.log4j.Logger;

import xtractor.schemaConverter.xer.xerConstructs.XERAttribute;
import xtractor.schemaConverter.xer.xerConstructs.XERCompoundConstruct;
import xtractor.schemaConverter.xer.xerConstructs.XEREntity;
import xtractor.schemaConverter.xer.xerConstructs.XERExplicitRelationship;
import xtractor.schemaConverter.xer.xerConstructs.XERForeignKey;
import xtractor.schemaConverter.xer.xerConstructs.XERGeneralization;
import xtractor.schemaConverter.xer.xerConstructs.XERImplicitRelationship;
import xtractor.schemaConverter.xer.xerConstructs.XERMixedEntity;
import xtractor.schemaConverter.xer.xerConstructs.XERPrimaryKey;
import xtractor.schemaConverter.xer.xerConstructs.XERRelationship;

/**
 * @author ss401
 * Factory for creating all XER Components.
 * Note that it is the factory's responsibility to register additions of any XERCompoundConstruct 
 * with the model 
 */
public class XERFactory {

	Logger logger = Logger.getLogger(this.getClass());
	XERBuilder xerBuilder;
	XERModel model;

	/**
	 * The builder is required so that the factory has access to the model
	 * @param builder - The XERBuilder constructing the XER model
	 */
	public XERFactory(XERBuilder builder) {
		this.xerBuilder = builder;
	}

	/**
	 * @return - A new XER Model
	 */
	public XERModel createXERModel(String modelName) {
		this.model = new XERModel(modelName, this);
		return model;
	}

	/**
	 * 
	 * @param name - the name of the entity
	 * @return - A new entity with the given name 
	 */
	public XEREntity createXEREntity(String name) {
		// Do not create an entity again if the entity already exists
		if (model.entityExists(name)) {
			return model.getEntity(name);
		}
		else {

			XEREntity entity = new XEREntity(name, xerBuilder);
			this.model.addEntity(entity);
			return entity;
		}
	}

	//	/**
	//	 * Note that the keyAttribute constitute only part of the foreign key 
	//	 * @param xerRelationship
	//	 * @param keyAttribute
	//	 * @return
	//	 */
	//	public XERForeignKey createXERForeignKey(XERRelationship xerRelationship, XERAttribute keyAttribute) {
	//		//new XERForeignKey(xerRelationship.getName(), keyAttribute, foreignEntity, xerRelationship.getMinOccurs());
	//		return new XERForeignKey(xerRelationship, keyAttribute);
	//	}
	//
	//	/**
	//	 * The key is automatically added to 'entity'
	//	 * This should only be used for creating keys for entities which do not have any key specified
	//	 * @param entity - The XER Entity which has no primary key defined in the xml schema
	//	 * @return The XERPrimaryKey attribute
	//	 */
	//	public XERPrimaryKey assignAutoXERPrimaryKey(XEREntity entity) {
	//		//foreignEntity.addKeyAttribute(new XERPrimaryKey(foreignEntity));
	//		XERPrimaryKey key = new XERPrimaryKey(entity);
	//		entity.addKeyAttribute(key);
	//		return key;
	//	}
	//	/**
	//	 * @param attribute - The JDom model of the xsd:attribute which acts as a primary key
	//	 * @param entity - The entity that the attribute acts as the primary key for
	//	 * @return - An XERPrimaryKey model of the attribute
	//	 */
	//	public XERPrimaryKey createXERPrimaryKey(Element xsdAttribute, XEREntity entity) {
	//		//entity.addKeyAttribute(new XEaRAttribute(attribute));
	//		//XERPrimaryKey key = new XERPrimaryKey(xsdAttribute, entity);
	//		XERPrimaryKey key = new XERPrimaryKey(entity);
	//		entity.addKeyAttribute(key);
	//		return key;
	//	}

	/**
	 * @param generalizationName - The name of the generalization
	 * @param entities - All the entities to which the generalization apply to
	 * @param minOccurs - The minimum number of times the choice collection (AS A WHOLE) can be selected
	 * @param maxOccurs - The maximum number of times the choice collection (AS A WHOLE) can be selected
	 * @return - An XER Generalization
	 */
	//TODO - this shouldn't just apply to entities, because a generalization may apply to simple elements too
	public XERGeneralization createXERGeneralization(
		String generalizationName,
		Collection entities,
		int minOccurs,
		int maxOccurs) {
		XERGeneralization generalization = new XERGeneralization(generalizationName, entities, minOccurs, maxOccurs, xerBuilder);

		model.addGeneralization(generalization);
		return generalization;
	}

	/**
	 * @param name - The name to be given to the XER Attribute
	 * @param typeName - The XSD type of the attribute
	 * @param minOccurs - The minOccurs value of the simple element or attribute
	 * @param maxOccurs - The maxOccurs value of the simple element or attribute
	 * @return
	 */
	private XERAttribute createXERAttribute(String name, String typeName, int minOccurs, int maxOccurs,int origination) {
		return new XERAttribute(name, typeName, minOccurs, maxOccurs, xerBuilder,origination);
	}

	public XERAttribute createXERAttributeFromXSDAttribute(String name, String typeName, int minOccurs, int maxOccurs) {
		return createXERAttribute(name, typeName, minOccurs, maxOccurs,XERAttribute.XSD_ATTRIBUTE);
	}
	public XERAttribute createXERAttributeFromXSDElement(String name, String typeName, int minOccurs, int maxOccurs) {
		return createXERAttribute(name, typeName, minOccurs, maxOccurs,XERAttribute.XSD_ELEMENT);
	}

	/**
	 * Creates a CONTAINMENT relationship between two XERCompoundConstructs
	 * (This does NOT have to be between entities; it could be between generalizations)
	 * @param parent - An XERCompoundConstruct
	 * @param child - An XERCompoundConstruct	 
	 * @param minOccurs - the minimum number of times the child occur 'in' the parent
	 * @param maxOccurs - the maximum number of times the child occur 'in' the parent
	 * @return - An XERRelationship between the child and the parent
	 * Note that the 'backward' cardinality will always be 1:1 (I think) due to the tree structure of an XML data
	 */
	public XERRelationship createXERImplicitRelationship(
		XERCompoundConstruct parent,
		XERCompoundConstruct child,
		int minOccurs,
		int maxOccurs) {

		if (child instanceof XEREntity) {
			((XEREntity) child).setImplicitParent(parent);
		}
		else if (child instanceof XERGeneralization) {
			// Do nothing?
		}

		else {
			logger.warn("Unimplemented case.");
		}

		XERImplicitRelationship xerRelationsihp = new XERImplicitRelationship(parent, child, minOccurs, maxOccurs);

		model.addRelationship(xerRelationsihp);
		return xerRelationsihp;

	}

	/**
	 * Creates a referential relationship but where the child and parent can exist independently
	 * @param parent - The XER Entity making the reference
	 * @param parentsForeignKeys - The XERForeignKey attributes of the parent which refer to the child
	 * @param child - XER Entity being referenced
	 * @param minOccurs
	 * @param maxOccurs
	 * @return
	 */
	public XERRelationship createXERExplicitRelationship(
		XEREntity parent,
		Collection parentsForeignKeys,
		XEREntity child,
		int minOccurs,
		int maxOccurs) {

		XERExplicitRelationship xerRelationsihp =
			new XERExplicitRelationship(parent, parentsForeignKeys, child, minOccurs, maxOccurs);
		model.addRelationship(xerRelationsihp);
		return xerRelationsihp;

	}

	/**
	 * @param attribute - An attribute which is to become a primary key
	 * @return - An XERPrimaryKey construct
	 */
	public XERPrimaryKey createXERPrimaryKey(XERAttribute attribute, XEREntity entity) {
		return new XERPrimaryKey(attribute, entity, xerBuilder);
	}

	/** 
	 * Note that the attribute may not be the full part of a foreign key
	 * @param attribute - The XERAttribute which we have discovered is PART OF a foreign key
	 * @param child - The XER Compound Construct which is BEING referenced
	 */
	public XERForeignKey createXERForeignKey(XERAttribute attribute, XERCompoundConstruct child) {
		return new XERForeignKey(attribute, child, xerBuilder);
	}

	/**
	 * @return
	 */
	public XEREntity createXERMixedEntity(String name) {
		// Do not create an entity again if the entity already exists
		if (model.entityExists(name)) {
			return model.getEntity(name);
		}
		else {
			XERMixedEntity mixedEntity = new XERMixedEntity(name, xerBuilder);
			this.model.addEntity(mixedEntity);
			return mixedEntity;
		}
	}

}
