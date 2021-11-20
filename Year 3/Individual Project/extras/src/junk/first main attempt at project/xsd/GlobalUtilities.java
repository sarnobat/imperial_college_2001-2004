package xsd;

/*
 * Created on 24-Feb-2004
 *
 */

/**
 * @author ss401
 *
 */
public class GlobalUtilities {

	static int sequenceCount = 0;

	/**
	 * Utility method for removing stray comma from the end of a list. Useful for toString() methods
	 * @param csvList - A list as a String separated by commas. e.g. "one,two,three,"  
	 * @return
	 */
	public static String removeLastColon(String csvList){
		return csvList.substring(0,csvList.length()-1);
	}
	/**
	 * This is NOT a passive read
	 * @return - the number of Sequence model groups in the schema. Useful for assigning each table a unique
	 * id
	 */
	public static int getNextSequenceId(){
		sequenceCount++;
		return sequenceCount-1;	
	}
	
}
