/*
 * Created on 30-Oct-2003
 *
 */
package yams.processor;
import yams.exceptions.YAMSIllegalArgumentException;

/**
 * A MIPSBitstring wrapper for words.
 * @author sw00
 */
public class MIPSWord {

	public static final int SIZE = 32;
	private MIPSBitstring bitstring;

	/**
	 * Create a new word
	 */
	public MIPSWord() {
		bitstring = new MIPSBitstring(SIZE);
	}

	/** Create a new word with a starting value
	 * @param value
	 */
	public MIPSWord(int value) {
		this();
		// negative checking omitted
		set(value);
	}

	/**
	 * Get the word as an int
	 * @return word value
	 */
	public int get() {
		return bitstring.toInt();
	}

	/** Set the word
	 * @param value
	 */
	public void set(int value) {
		// negative checking omitted
		for(int i = 0; i < SIZE; i++) {
			bitstring.setBit(i, (value >> i) & 1);
		}
	}
	
	/** Returns the underlying bitstring
	 * @return bitstring
	 */
	public MIPSBitstring getBitstring() {
		return bitstring;
	}
			
	/** Get a bit in the word
	 * @param bitnumber
	 * @return the bit
	 */
	public int getBit(int bitnumber) {
		if((bitnumber < 0) || (bitnumber > SIZE - 1)) {
			// 0 .. SIZE - 1 are only permissible values for 'bitnumber'
			throw new YAMSIllegalArgumentException("bitnumber out of range");
		}
		return bitstring.getBit(bitnumber); 
	}
	
	/** Set a bit in the word
	 * @param bitnumber
	 * @param value
	 */
	public void setBit(int bitnumber, int value) {
		if((bitnumber < 0) || (bitnumber > SIZE - 1)) {
			// 0 .. SIZE - 1 are only permissible values for 'bitnumber'
			throw new YAMSIllegalArgumentException("bitnumber out of range");
		}
		if((value < 0) || (value > 1)) {
			// 0 or 1 are only permissible values for 'value'
			throw new YAMSIllegalArgumentException("value out of range");
		}
		bitstring.setBit(bitnumber, value);				
	}

	public String toString() {
		return bitstring.toString();
	}

}
