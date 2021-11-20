package yams;

import java.io.*;

public abstract class YAMSController {
	
	public abstract void start(String args[]);
	public abstract void regChanged(String regID);
	public abstract void memoryChanged(int address);
	public abstract void updateStatistics();
	
	/**
	 * @param fileName
	 * @return returns a StringBuffer containing the text from the file
	 */
	protected static StringBuffer loadCode(File fileName) throws FileNotFoundException
	{
		FileReader inFile;
		BufferedReader inBuf;
		inFile = new FileReader(fileName);
		inBuf = new BufferedReader(inFile);
		StringBuffer outBuf = new StringBuffer();
		
		// load inBuf into outBuf
		try {
			String line;
			while ( (line = inBuf.readLine()) != null )
				outBuf.append(line + "\n");
		}
		catch (IOException e) {
			// some other error
			e.printStackTrace();
		}
		
		return outBuf;
	}
	
}
