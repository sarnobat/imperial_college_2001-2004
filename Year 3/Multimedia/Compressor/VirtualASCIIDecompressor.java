import java.io.File;

/*
 * Created on 17-Feb-2004
 *
 */

/**
 * @author ss401
 *
 */
public class VirtualASCIIDecompressor {
	
	static String cipherText = "1100111100000010110011001011001100000101010111101001000000001000110001100000101000001011000011001100001000000111011001101101000111010001000001100000010010010111110011110000110100001011100111100000111100001011000000010110101001101110110010110000100100010000000100010001001010101100000100001100100111010001000001100000110110010100000001010000110100000110100001111011001010100100000001100000000000000101000010001100011011000011000001111100111001110001010110011101000010001011100001111011001010100100100011001100001101011000000001011100111000011001101011100001011001100010011100110000011011000011101010111011001100000110011111001100100100001101000100110111100010011011110000111000101010010011101101111010001110000110100100100110000101011111011111010111000100010101110011000000010100000011000101010000110110011011000100000000101100000110000101011100110000000101000000110001010100001101100110110001000000001011011101101001110000001100110001011010111100011100110011000000010100000011000101010000110110100010100110111101000111010001000001100000101001101101000101100001000001111001101111101011100010100000101001000000001000001001000001010001010111010001110100011010101101101100101100000000001100010101000011011010110001100110010011101001111100000110000010000000000000000011000110001001000110100011110100000111110011010001001101001100010000011110001110111001111001010101000001100000000100000011000100100000110010101111101110001000110001111011000010011001011000001100000010010000010111001110000110011010111000010110011000100111001100001000101000111100010100000110110000100000010100001000000000010000000000100011000101101100100000110101101001001010000000000010110011001011001100000101010111101011001100011010110011110101110011000101101010111011110100010100101111010000100110101111101110001011011101011011100101010010010010100001101011010000100100000100000001010000100000000110011001110001011000010101101100110001101011001111010111001100010110101011101111011011110101110111110010001001111000001111101110100111100100010001000011001010111000010110000101011101000111010001101010110110110000000001101011110010010100000111000010010110100000011100101011100000010111001000101011010001010100001101100110110001000000001011011101101100011011000101000001111000101010010011000111001010111000000101110010000000110011001111110001101100001100000111110011100111000101011001110100001100100110110111000100110001010010010001010011110001101010101111100001111100001110110100101101011100100100011001100101000101100000010010011110101001100101100100000110000000100110011010100110001001001101110110010010100100100001100000000001101000001111000100000111100011101110011110010101010000011000000001000001000000110001000000101111100011111000111101101100110000110111001101011001000001100000001001100110101001100001001101000011010000011000010101100101100000010110001010010101001000100000000110001001100001001110000000110100011101001011001011000000100001000001000111110011101000011110110010101001000000011000011010100101000101100000010000000001100001010010111101000010011010111110111000101101110101101100000001000011000000000000011000110100000111110010001000000001101000011100000110000100000001001100010010110010000000110100000110000100000000001100011000100100011010001110010001000000000000010110001010010101001000100000000110001001101011101000001101101010111101000001100111000101100001010100000110000101001001010000010010101000000100010000000100000011000100000010111110001111101011111100001100000011010000011000010000000000110001100010010001101000111101000000010110000011000101010110110111011110011100110111001011100001100001001110100111100110010110010000011000000010011001101010011000100111100001000010100011110100000111110011010001110100011100110100000111011001101101000111010001000001100101010110110111001001110010010100011100001001001010000110101101000010010000010000000101000010000000011000011010000001001101000111010001010101011011011101111001110011011011010010101111101110000000011000101000110000001010101000010100000000000001000011010001110100010000011000001010011011010001011000010000100010100111100010011011110000111000101010010011";
	public static void main(String[] args) {
		if (args.length == 1) {
			cipherText = args[0];
		}
		LookupTable table = new LookupTable(new File("table.txt"));
		
		for (int i = 0; i < cipherText.length()-8; i += 8) {
			//byte byteValue = (byte) cipherText.charAt(i);
			String byteStr = cipherText.substring(i,i+8);
			
			System.out.print(table.get(VirtualASCIICompressor.binaryStringToInt(byteStr)));
		}
		//String[] tokens = "t<-><-".split("<->");
			
		//String binary = Integer.toBinaryString(1);
		
		return;
		
	}
	
	
}