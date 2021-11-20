/*
 * Created on 06-Nov-2003
 */
package yams.parser;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Models the sequence of lines of users instructions.
 * 
 * @author qq01
 */
public class LineList {
	
	private int currentLine;
	
	/**
	 * Points to the last line of the list, can also act as a total line count.
	 */
	private int lastLine;
	private Map lines;

	public LineList() {
		lines = new TreeMap();
		currentLine = 0;
		lastLine = 0;
	}

	/**
	 * Appends a line at the end of the list of instructions.
	 * 
	 * @param line the <code>Line</code> to be appended at the end of the list
	 */
	protected void appendLine(Line line) {
		lastLine++;
		lines.put(new Integer(lastLine), line);
	}

	/**
	 * Gets a specific line.
	 * 
	 * @param lineNumber specify the line number
	 * @return the <code>Line</code> object
	 */
	public Line getLine(int lineNumber) {
		return (Line) lines.get(new Integer(lineNumber));
	}

	/**
	 * Gets the next line is the users instruction file.
	 * 
	 * @return the <code>Line</code> object
	 */
	public Line nextLine() {
		currentLine++;
		return (Line) lines.get(new Integer(currentLine));
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		
		Iterator it = lines.values().iterator();
		
		while(it.hasNext()){
			Line l = (Line) it.next();
			buffer.append(l.toString());
			buffer.append("\n");
		}
		return buffer.toString();
	}
	
	/**
	 * Gives a total line count.
	 * 
	 * @return total line count
	 */
	public int totalLines(){
		return lastLine;
	}
}
