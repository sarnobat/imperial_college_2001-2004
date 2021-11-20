/*
 * Created on 2003-12-5
 */
package yams.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author Qian Qiao(qq01)
 */
public class FileReader {
	public InputStream readFile(String file) throws FileNotFoundException {
		File f = new File(file);

		if (f.exists()) {
			return new FileInputStream(f);
		} else {
			return ClassLoader.getSystemResourceAsStream(file);
		}
	}

	public static void main(String args[]) {
		FileReader cf = new FileReader();
		try {
			InputStream is = cf.readFile("Instruction_file.xml");
			System.out.println(is.toString());
		} catch (FileNotFoundException e) {
			System.out.println(e.toString());
		}
	}
}
