/*
 * Created on 30-Oct-2003
 *
 */
package yams.processor;
import yams.exceptions.YAMSIllegalArgumentException;

/**
 * @author sw00
 *
 */
public class MIPSWord {

	public static final int SIZE = 32;
	private MIPSBitstring bitstring;

	public MIPSWord() {
		bitstring = new MIPSBitstring(SIZE);
	}

	public MIPSWord(int value) {
		this();
		// negative checking omitted
		set(value);
	}

	public int get() {
		return bitstring.toInt();
	}

	public void set(int value) {
		// negative checking omitted
		for(int i = 0; i < SIZE; i++) {
			bitstring.setBit(i, (value >> i) & 1);
		}
	}
	
	public MIPSBitstring getBitstring() {
		return bitstring;
	}
			
	public int getBit(int bitnumber) {
		if((bitnumber < 0) || (bitnumber > SIZE - 1)) {
			// 0 .. SIZE - 1 are only permissible values for 'bitnumber'
			throw new YAMSIllegalArgumentException("bitnumber out of range");
		}
		return bitstring.getBit(bitnumber); 
	}
	
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
