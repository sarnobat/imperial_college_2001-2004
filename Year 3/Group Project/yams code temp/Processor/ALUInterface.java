/*
 * Created on 26-Oct-2003
 *
 */
package ic.doc.yams;

public interface ALUInterface {

	// the following are arithmetic & logical operations 
	public MIPSWord abs(MIPSWord src1);
	public MIPSWord add(MIPSWord src1, MIPSWord src2);
	public MIPSWord add_without_overflow(MIPSWord src1, MIPSWord src2);
	public MIPSWord and(MIPSWord src1, MIPSWord src2);
	public void div(MIPSWord src1, MIPSWord src2, MIPSWord desthi, MIPSWord destlo);
	public void div_unsigned(MIPSWord src1, MIPSWord src2, MIPSWord desthi, MIPSWord destlo);
	public void div_with_overflow(MIPSWord src1, MIPSWord src2, MIPSWord desthi, MIPSWord destlo);
	public void div_unsigned_with_overflow(MIPSWord src1, MIPSWord src2, MIPSWord desthi, MIPSWord destlo);
	public MIPSWord mul(MIPSWord src1, MIPSWord src2);
	public MIPSWord mul_with_overflow(MIPSWord src1, MIPSWord src2);
	public MIPSWord mul_unsigned_with_overflow(MIPSWord src1, MIPSWord src2);
	public void mult(MIPSWord src1, MIPSWord src2, MIPSWord desthi, MIPSWord destlo);
	public void mult_unsigned(MIPSWord src1, MIPSWord src2, MIPSWord desthi, MIPSWord destlo);
	public MIPSWord neg(MIPSWord src);
	public MIPSWord neg_without_overflow(MIPSWord src);
	public MIPSWord nor(MIPSWord src1, MIPSWord src2);
	public MIPSWord not(MIPSWord src);
	public MIPSWord or(MIPSWord src1, MIPSWord src2);
	public MIPSWord rem(MIPSWord src1, MIPSWord src2);
	public MIPSWord rem_unsigned(MIPSWord src1, MIPSWord src2);
	public MIPSWord rol(MIPSWord src1, MIPSWord src2);
	public MIPSWord ror(MIPSWord src1, MIPSWord src2);
	public MIPSWord sll(MIPSWord src1, MIPSWord src2);
	public MIPSWord sllv(MIPSWord src1, MIPSWord src2);
	public MIPSWord sra(MIPSWord src1, MIPSWord src2);
	public MIPSWord srav(MIPSWord src1, MIPSWord src2);
	public MIPSWord srl(MIPSWord src1, MIPSWord src2);
	public MIPSWord srlv(MIPSWord src1, MIPSWord src2);
	public MIPSWord sub(MIPSWord src1, MIPSWord src2);
	public MIPSWord sub_without_overflow(MIPSWord src1);
	public MIPSWord xor(MIPSWord src1, MIPSWord src2);

	public MIPSWord sign_extend(MIPSByte src);	
	// returns the byte sign extended to a 32-bit word
	
	public MIPSWord sign_extend(MIPSHalfword src);	
	// returns the halfword sign extended to a 32-bit word
	
	public MIPSWord zero_extend(MIPSByte src);	
	// returns the byte zero extended to a 32-bit word
	
	public MIPSWord zero_extend(MIPSHalfword src);	
	// returns the halfword sign extended to a 32-bit word

	// comparison methods
	public boolean isEqual(MIPSWord src1, MIPSWord src2);
	public boolean isGreaterThanEqual(MIPSWord src1, MIPSWord src2);
	public boolean isGreaterThanEqual_unsigned(MIPSWord src1, MIPSWord src2);
	public boolean isGreaterThan(MIPSWord src1, MIPSWord src2);
	public boolean isGreaterThan_unsigned(MIPSWord src1, MIPSWord src2);
	public boolean isLessThanEqual(MIPSWord src1, MIPSWord src2);
	public boolean isLessThanEqual_unsigned(MIPSWord src1, MIPSWord src2);
	public boolean isLessThan(MIPSWord src1, MIPSWord src2);
	public boolean isLessThan_unsigned(MIPSWord src1, MIPSWord src2);
	public boolean isNotEqual(MIPSWord src1, MIPSWord src2);
}
