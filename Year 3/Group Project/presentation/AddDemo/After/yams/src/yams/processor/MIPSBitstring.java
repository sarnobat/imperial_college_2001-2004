package yams.processor;
import yams.exceptions.YAMSIllegalArgumentException;

/*
 * Created on 31-Oct-2003
 *
 */


/**
 * @author sw00
 *
 */
public class MIPSBitstring {

	private int length;
	private int[] bits;

	public MIPSBitstring(int length) {
		this.length = length;
		bits = new int[length];
		for(int i = 0; i < length; i++) {
			setBit(i, 0);	
		}
	}
	
	public int getBit(int bitnumber) {
		return bits[bitnumber];
	}
	
	public void setBit(int bitnumber, int value) {
		if((bitnumber < 0) || (bitnumber >= length)) {
			// 0 .. length-1 are only permissible values for 'bitnumber'
			throw new YAMSIllegalArgumentException("bitnumber out of range");
		}
		if((value < 0) || (value > 1)) {
			// 0 .. 1 are only permissible values for 'value'
			throw new YAMSIllegalArgumentException("value out of range");
		}
		bits[bitnumber] = value;
	}

	public int getLength() {
		return length;
	}

	public static MIPSBitstring repeat(int value, int repeatcount) {
		if((value < 0) || (value > 1)) {
			// 0 .. 1 are only permissible values for 'value'
			throw new YAMSIllegalArgumentException("value out of range");
		}
		if(repeatcount < 1) {
			// 'repeatcount' must be at least 1
			throw new YAMSIllegalArgumentException("repeatcount out of range");
		}
		MIPSBitstring bitstring = new MIPSBitstring(repeatcount);
		for(int i = 0; i < repeatcount; i++) {
			bitstring.setBit(i, value);			
		}
		return bitstring;
	}
	
	public static MIPSBitstring extract(MIPSWord word, int lowbit, int highbit) {
		if((lowbit < 0) || (lowbit > 31) || (lowbit >= highbit)) {
			// invalid value for 'lowbit'
			throw new YAMSIllegalArgumentException("lowbit out of range");
		}
		if((highbit < 0) || (highbit > 31) || (highbit <= lowbit)) {
			// invalid value for 'highbit'
			throw new YAMSIllegalArgumentException("highbit out of range");
		}
		MIPSBitstring bitstring = new MIPSBitstring(highbit - lowbit + 1);
		int j = 0;
		for(int i = lowbit; i <= highbit; i++) {
			bitstring.setBit(j, word.getBit(i));
			j++;			
		}
		return bitstring;
	}
	
	public MIPSBitstring extract(int lowbit, int highbit) {
		if((lowbit < 0) || (lowbit > length) || (lowbit >= highbit)) {
			// invalid value for 'lowbit'
			throw new YAMSIllegalArgumentException("lowbit out of range");
		}
		if((highbit < 0) || (highbit > length) || (highbit <= lowbit)) {
			// invalid value for 'highbit'
			throw new YAMSIllegalArgumentException("highbit out of range");
		}
		MIPSBitstring bitstring = new MIPSBitstring(highbit - lowbit + 1);
		int j = 0;
		for(int i = lowbit; i <= highbit; i++) {
			bitstring.setBit(j, getBit(i));
			j++;			
		}
		return bitstring;
	}
		
	public static MIPSBitstring concatenate(MIPSBitstring left, 
											MIPSBitstring right) {
		int combinedlength = left.getLength() + right.getLength();
		MIPSBitstring bitstring = new MIPSBitstring(combinedlength);
		int j = 0;
		for(int i = 0; i < right.getLength(); i++) {
			bitstring.setBit(j, right.getBit(i));
			j++;			
		}
		for(int i = 0; i < left.getLength(); i++) {
			bitstring.setBit(j, left.getBit(i));
			j++;			
		}
		return bitstring;
	}

	public MIPSWord toMIPSWord() {
		int value = toInt();
		return new MIPSWord(value);
	}
	
	public int signExtend() {
		int signbit = getBit(length - 1);
		MIPSBitstring highbits = MIPSBitstring.repeat(signbit, MIPSWord.SIZE - length);
		MIPSBitstring extended = MIPSBitstring.concatenate(highbits, this); 
		return extended.toInt();
	}

	public int toInt() {
		// NB this is not sign extended if the bitstring is less than 32 bits long
		int total = 0;
		for(int i = 0; i < length; i++) {
			total += getBit(i) * (1 << i); 
		}
		return total;
	}

	public String toString() {
		StringBuffer str = new StringBuffer();
		for(int i = length - 1; i >= 0; i--) {
			str.append(getBit(i));		
		}
		return str.toString();
	}

}




