/*
 * Created on 24-Feb-2004
 *
 */
package xtractor.schemaConverter.rdb;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import xtractor.schemaConverter.rdb.parsers.SystemTableHandler;
import xtractor.schemaConverter.rdb.parsers.XERAttributeParser;
import xtractor.schemaConverter.rdb.parsers.XEREntityParser;
import xtractor.schemaConverter.rdb.parsers.XERGeneralizationParser;
import xtractor.schemaConverter.rdb.parsers.XERRelationshipParser;
import xtractor.schemaConverter.xer.XERModel;
import xtractor.schemaConverter.xer.xerConstructs.XERAttribute;
import xtractor.schemaConverter.xer.xerConstructs.XERCompoundConstruct;
import xtractor.schemaConverter.xer.xerConstructs.XERConstruct;
import xtractor.schemaConverter.xer.xerConstructs.XEREntity;
import xtractor.schemaConverter.xer.xerConstructs.XERGeneralization;
import xtractor.schemaConverter.xer.xerConstructs.XERRelationship;
import xtractor.schemaConverter.xsd.XSDReader;
/**
 * @author ss401
 * Takes an XER model and generates a relational database model 
 */
public class RDBBuilder {

	final String SYSTEM_PREFIX = "SYSTEM_";
	final String DELIMITER = "\t";
	Logger logger = Logger.getLogger(this.getClass());
	Writer out;
	XERModel model;
	SQLMapper sqlMapper;
	String schemaName;

	// The entities whose tables have been created.
	Collection parsedEntities;

	// parsers
	XEREntityParser entityParser;
	XERAttributeParser attributeParser;
	XERGeneralizationParser generalizationParser;
	XERRelationshipParser relationshipParser;

	SystemTableHandler systemTableHandler;

	/**
	 * @param xerModel - The XER model to convert to a relational schema
	 * @param schemaFile
	 */
	public RDBBuilder(XERModel xerModel, File schemaFile, Writer out) {
		this.model = xerModel;
		this.out = out;
		XSDReader schemaWalker = new XSDReader(schemaFile);
		this.sqlMapper = new SQLMapper(schemaWalker.getRootElement().getNamespace().getPrefix());
		this.schemaName = schemaFile.getName().split(".x")[0];

		this.parsedEntities = new LinkedList();

		// Initialize parsers
		this.systemTableHandler = new SystemTableHandler(this.schemaName);
		this.attributeParser = new XERAttributeParser(this);
		this.entityParser = new XEREntityParser(this);
		this.generalizationParser = new XERGeneralizationParser(this);
		this.relationshipParser = new XERRelationshipParser(this);

	}

	/**
	 * @param out - The place to write the SQL code
	 */
	public void build() throws IOException {

		// Make sure the database schema doesn't exist already
		out.write("DROP SCHEMA " + schemaName + " CASCADE;\n");

		// Delete the 'well known' table's entry for the schema
		out.write("DELETE FROM XTractor_schemas WHERE name='" + schemaName + "';\n");
		out.write("INSERT INTO XTractor_schemas(name) VALUES ('" + schemaName + "');\n");

		// Declare a new database schema
		out.write("CREATE SCHEMA " + schemaName + ";\n\n");

		// Create system tables
		/*out.write(systemTableHandler.createElementLocation());
		out.write(systemTableHandler.createMixedEntitySummary());
		out.write(systemTableHandler.createCharacterDataElements());*/
		createSystemTables();

		// Create a sequence so we can identify each element uniquely (in an element schema)
		out.write("CREATE SEQUENCE ss.id MINVALUE 1;\n");

		// We must create a table saying which table references which,
		// otherwise we don't know which tables to populate when expressing a containment 
		// relationship (at least in some cases such as a generalization)
		//out.write(systemTableHandler.createGeneralization());
		Collection generalizations = model.getGeneralizations();

		out.flush();

		//TODO: Actually, the root entity shouldn't be treated like the rest of them.
		// Parse the entities
		Collection entities = model.getEntities();
		for (Iterator iter = entities.iterator(); iter.hasNext();) {
			XEREntity xerEntity = (XEREntity) iter.next();
			entityParser.parseEntity(xerEntity);
			out.write("\n\n");
		}

		/*
		 * Parse the generalizations 
		 */
		for (Iterator iter = generalizations.iterator(); iter.hasNext();) {
			XERGeneralization generalization = (XERGeneralization) iter.next();
			out.write(generalizationParser.parseGeneralization(generalization));
			out.write("\n\n");

		}

		// Parse the relationships
		Collection relationships = model.getRelationships();
		for (Iterator iter = relationships.iterator(); iter.hasNext();) {
			XERRelationship relationship = (XERRelationship) iter.next();
			out.write(relationshipParser.parseRelationship(relationship));
			out.write("\n\n");
		}

		out.write(systemTableHandler.getPendingSystemData());
		out.write(getAliases());

		/* 
		 * The following cannot be done before the tables have been created:
		 * 		- Creating the complete view of the element schema tables is done
		 * 		- Creating an id to table mapping
		 */

		out.flush();

	}

	/**
	 * @return
	 */
	private String getAliases() throws IOException {
		String sql = "";
		for (Iterator iter = model.getAliases().keySet().iterator(); iter.hasNext();) {
			XERConstruct xerConstruct = (XERConstruct) iter.next();
			
			//TODO: Haven't handled generalizations. Will involve adjusting the generalizations catalog
			if (xerConstruct instanceof XERAttribute) {
				sql += systemTableHandler.addAttributeAlias(xerConstruct.getAlias(), (XERAttribute) xerConstruct);
			}
			else if (xerConstruct instanceof XEREntity) {
				sql += systemTableHandler.addElementAlias(xerConstruct.getAlias(),  (XEREntity) xerConstruct);
			}
			else{
				logger.warn("Unimplemented case.");
			}

		}
		return sql + "\n";
	}

	/**
	 * 
	 */
	private void createSystemTables() throws IOException {
		out.write(systemTableHandler.getTableCreationSQL());
		
		XERCompoundConstruct root = model.getRootEntity();
		if(root instanceof XEREntity){
			out.write(systemTableHandler.insertElementInformation((XEREntity) root, null, null, "complex"));	
		}
		else{
			logger.warn("Unimplemented case.");
		}
		
	}

	/**
	 * @return - The SQL output stream writer
	 */
	public Writer getWriter() {
		return out;
	}

	/**
	 * Convenience method
	 * @return
	 */
	public XERAttributeParser getAttributeParser() {
		return this.attributeParser;
	}

	/**
	 * @return - The default delimiter at the lowest level (e.g. a tab)
	 */
	public String getDelimiter() {
		return this.DELIMITER;
	}

	/**
	 * @return - The SQL mapper which handles XSD to SQL type conversions
	 */
	public SQLMapper getSqlMapper() {
		return sqlMapper;
	}

	/**
	 * @return - The name of the xsd file minus the extension, i.e. the name of the table space
	 */
	public String getPrefix() {
		return this.schemaName;
	}

	/**
	 * @return - A collection of strings, where each string is the qualified name of a table which has been created
	 */
	public Collection getParsedEntities() {
		return this.parsedEntities;
	}

	/**
	 * @return
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * @return
	 */
	public SystemTableHandler getSystemTableHandler() {
		return systemTableHandler;
	}

}
