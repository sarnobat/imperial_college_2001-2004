/*
 * Created on 2003-11-24
 */
package yams.parser;

/**
 * Models the different types of addressing modes. Addressing operands are slightly more
 * complicated then the other operands and they can be a combination of <code>immediate</code>,
 * <code>label</code> and <code>register</code>.
 * 
 * @author Qian Qiao(qq01)
 */
public class AddressingOperand extends Operand {

	/**
	 * Addressing Operand of the form <code>immediate</code>.
	 */
	public static final int IMMEDIATE = 0;

	/**
	 * Addressing Operand of the form <code>immediate(register)</code>.
	 */
	public static final int IMMEDIATE_REGISTER = 1;

	/**
	 * Addressing Operand of the form <code>label</code>.
	 */
	public static final int LABEL = 2;

	/**
	 * Addressing Operand of the form <code>label +/- immediate</code>.
	 */
	public static final int LABEL_PLUS_IMMEDIATE = 3;

	/**
	 * Addressing Operand of the form <code>label +/- immediate (register)</code>.
	 */
	public static final int LABEL_PLUS_IMMEDIATE_REGISTER = 4;

	/**
	 * Addressing Operand of the form <code>(register)</code>.
	 */
	public static final int REGISTER = 5;

	private int immediate;

	private String label;

	private String register;

	private int type;

	private AddressingOperand() {
		type = -1;
	}

	/**
	 * Constructs an addressing operand representing the form <code>immediate</code>.
	 * 
	 * @param type type of the operand
	 * @param immediate immediate value
	 */
	public AddressingOperand(int type, int immediate) {
		this.type = IMMEDIATE;
		this.immediate = immediate;
	}

	/**
	 * Constructs an addressing operand representing the form <code>immedate (register)</code>.
	 * 
	 * @param type type of the operand
	 * @param immediate the immediate value
	 * @param register the register
	 */
	public AddressingOperand(int type, int immediate, String register) {
		this.type = IMMEDIATE_REGISTER;
		this.immediate = immediate;
		this.register = register;
	}

	/**
	 * Constructs an addressing operand representing <code>label</code> or <code>register</code>
	 * depending on what is specified by the <b>type</b> parameter.
	 * 
	 * @param type type of the operand. <b>Crucial</b>, as this distinguishes between label and register
	 * @param value value of the operand
	 */
	public AddressingOperand(int type, String value) {
		if (type == LABEL) {
			this.type = LABEL;
			this.label = value;
		} else {
			this.type = REGISTER;
			this.register = value;
		}
	}

	/**
	 * Constructs an addressing operand representing the form <code>label +/- immediate</code>
	 * 
	 * @param type type of the addressing operand
	 * @param label label value of the addressing operand
	 * @param immediate immediate value of the addressing operand
	 */
	public AddressingOperand(int type, String label, int immediate) {
		this.type = LABEL_PLUS_IMMEDIATE;
	}

	/**
	 * Constructs an addressing operand representing the form <code>label +/- immediate
	 * (register)</code>.
	 * 
	 * @param type type of the addressing operand
	 * @param label label value
	 * @param immediate immediate value
	 * @param register register value
	 */
	public AddressingOperand(
		int type,
		String label,
		int immediate,
		String register) {
		this.type = LABEL_PLUS_IMMEDIATE_REGISTER;
		this.label = label;
		this.immediate = immediate;
		this.register = register;
	}

	/**
	 * Returns the value of the immediate part of the operand.
	 * 
	 * @return immediate part of the operand
	 */
	public int getImmediate() {
		return immediate;
	}

	/**
	 * Returns label part of the operand.
	 * 
	 * @return label part of the operand
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Returns register part of the operand.
	 * 
	 * @return register part of the operand
	 */
	public String getRegister() {
		return register;
	}

	/* (non-Javadoc)
	 * @see yams.parser.Operand#getType()
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return whether this addressing operand has a immediate value.
	 */
	public boolean hasImmediate() {
		return (
			type == IMMEDIATE
				|| type == IMMEDIATE_REGISTER
				|| type == LABEL_PLUS_IMMEDIATE
				|| type == LABEL_PLUS_IMMEDIATE_REGISTER);
	}

	/**
	 * @return whether this addressing operand has a label value.
	 */
	public boolean hasLabel() {
		return (
			type == LABEL
				|| type == LABEL_PLUS_IMMEDIATE
				|| type == LABEL_PLUS_IMMEDIATE_REGISTER);
	}

	/**
	 * @return whether this addressing operand has a register value.
	 */
	public boolean hasRegister() {
		return (
			type == REGISTER
				|| type == IMMEDIATE_REGISTER
				|| type == LABEL_PLUS_IMMEDIATE_REGISTER);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		if (label != null) {
			buffer.append(label);
		}

		if (immediate != 0) {
			buffer.append(immediate);
		}

		if (register != null) {
			buffer.append("(" + register + ")");
		}

		return buffer.toString();
	}
}
