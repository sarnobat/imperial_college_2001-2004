import java.io.FileReader;
import java.io.FileWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/*
 * Created on 31-Jan-2004
 *
 */

/**
 * @author ss401
 *
 */

public class LatexTransform {

	private static TransformerFactory tf = null;

	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			System.out.println("Usage: java MIPSTransform XML XSL OUT ");
				
			} else {
			tf = TransformerFactory.newInstance();

			StreamSource xml = new StreamSource(new FileReader(args[0]));
			StreamSource xsl = new StreamSource(new FileReader(args[1]));
			StreamResult output = new StreamResult(new FileWriter(args[2]));

			Transformer t;
			try {
				t = tf.newTransformer(xsl);
				t.transform(xml, output);
			} catch (TransformerConfigurationException e) {
				System.out.println(e);				
			}

		}
	}
}
