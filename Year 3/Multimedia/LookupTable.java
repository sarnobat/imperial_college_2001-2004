import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
 * Created on 14-Feb-2004
 *
 */

/**
 * @author ss401
 *
 */
public class LookupTable {

	//List seqs = new ArrayList();
	Map seqs = new HashMap();
	Map ids = new HashMap();

	public static String entryDelimiter = ">-<";
	public static String pairDelimiter = "<->";
	/**
	 * This constructor is used by the compressor
	 *
	 */
	public LookupTable() {
	}
	/**
	 * This constructor is used by the decompressor
	 * @param table
	 */
	public LookupTable(File table) {

		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(table));

			String str;
			String acc = "";
			while ((str = in.readLine()) != null) {
				acc += str;
			}
			//StringTokenizer tokens = new StringTokenizer(acc, entryDelimiter, true);
			String[] entries = acc.split(entryDelimiter);
			for (int i = 0; i < entries.length; i++) {
				String[] entry = entries[i].split(pairDelimiter);
				seqs.put(new Integer(entry[0]),entry[1]);
			}
			
			System.out.print("");
			
		}
		catch (Exception e) {
			System.out.println(e);
			System.exit(-1);
		}

	}

	public void add(String s) {
		if (!seqs.containsValue(s)) {

			seqs.put(new Integer(seqs.values().size()), s);
			ids.put(s, new Integer(seqs.values().size()-1));
		}
		//return seqs.size();

	}
	public String get(int id) {
		return (String) seqs.get(new Integer(id));
	}
	public int getId(String s) {
		if (seqs.containsValue(s)) {

			return ((Integer) ids.get(s)).intValue();
		}
		else {
			return -1;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String s = "";
		for (int i = 0; i < seqs.values().size(); i++) {
			s += i + ":\t" + seqs.get(new Integer(i)) + "\n";

		}
		return s;
	}
	public boolean contains(String s) {
		if (seqs.containsValue(s)) {
			return true;
		}
		return false;
	}
	public Map cheat() {
		return seqs;
	}
	public int length() {
		return seqs.size();
	}

	public void outputToFile() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("table.txt"));
			for (Iterator iter = seqs.keySet().iterator(); iter.hasNext();) {
				Integer id = (Integer) iter.next();
				if (iter.hasNext()) {
					out.write(id + pairDelimiter + ((String) seqs.get(id)) + entryDelimiter);
				}
				else {
					out.write(id + pairDelimiter + ((String) seqs.get(id)));
				}
			}
			out.close();
		}
		catch (IOException e) {
		}

	}
}
