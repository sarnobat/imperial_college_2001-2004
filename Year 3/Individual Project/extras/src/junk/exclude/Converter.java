
/*
 * Created on 10-Feb-2004
 *
 */

/**
 * @author ss401
 *
 */
public class Converter {
	private static String FILEPATH;

	public static void main(String[] args) {
		//FILEPATH = "accounts.xsd";
		FILEPATH = "purchaseOrder.xsd";
		

		XSDSchemaManipulator schema = new XSDSchemaManipulator(FILEPATH);
		
		// Top level element
		String databaseName = schema.getRootElement();
		System.out.println("CREATE DATABASE " + databaseName);

		// Child Elements of top level element level Elements correspond to tables
		/*List topLevelElements = schema.getAttributesOfComplexType(databaseName);
		for (int i = 0; i < topLevelElements.size(); i++) {
			String tableName = (String) topLevelElements.get(i);
			List attributes = schema.getAttributes(tableName);
			String sql = "CREATE TABLE " + tableName + " " + "(";
			for (Iterator iter = attributes.iterator(); iter.hasNext();) {
				String attributeName = (String) iter.next();
				sql += attributeName + ",";
			}
			sql = sql.substring(0, sql.length() - 1);
			sql += ");";
			System.out.println(sql);
		}*/
	}
}