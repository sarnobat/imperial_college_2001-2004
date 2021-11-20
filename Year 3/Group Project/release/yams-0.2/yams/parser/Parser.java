/*
 * Created on 2003-11-1
 */
package yams.parser;

//import java.util.Vector;

/**
 * Parses the users file. Invokes the <code>Lexer</code> automatically.
 * 
 * @author Qian Qiao <a href="qq01@doc.ic.ac.uk">qq01@doc.ic.ac.uk</a>
 */
public class Parser {

	/**
	 * Dummy method for debugging.
	 * 
	 * @param args arguments
	 */
	public static void main(String[] args) {
		Parser parser = new Parser(new StringBuffer("causing error"));
		try {
			LineList lineList = parser.parse();
			System.out.println(lineList.toString());
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private Token currentToken;

	private int instructionCount = 0;

	private LineList lineList;
	private StringBuffer program;

	/**
	 * Constructs a new parser. If a parser is constructed this way, then the method
	 * <code>parse(StringBuffer Program)</code> should be used.
	 */
	public Parser() {
		lineList = new LineList();
	}

	/**
	 * Constructs a parser that parses the <code>program</code>.
	 * 
	 * @param program the input program
	 */
	public Parser(StringBuffer program) {
		this.program = program;
		lineList = new LineList();
	}

	private void match(Lexer lexer, int expected)
		throws ParseException, LexerException {
		if (currentToken.getType() == expected) {
			currentToken = lexer.nextToken();
		} else {
			throw new ParseException(
				currentToken,
				expected,
				lexer.getLineNumber());
		}
	}

	/**
	 * Parses the program.
	 * 
	 * @return a collection of the <code>Line</code> that represents the user's input program.
	 * @throws LexerException if the lexer finds an error
	 * @throws ParseException if the parser finds an error
	 */
	public LineList parse() throws LexerException, ParseException {

		Lexer lexer = new Lexer(program);

		currentToken = lexer.nextToken();

		Label label = null;

		Instruction instruction = null;

		Directive directive = null;

		boolean lineHasLabel = false;

		boolean lineHasInstruction = false;

		boolean lineHasDirective = false;

		while (currentToken.getType() != Lexer.EOF) {
			if (currentToken.getType() == Lexer.NEWLINE) {

				lineList.appendLine(
					new Line(
						lineList.totalLines() + 1,
						lexer.getOriginalLine()));

			} else {
				currentToken = lexer.nextToken();
				if (currentToken.getType() == Lexer.COLON) {

					lexer.pushTokenBack();
					lexer.pushTokenBack();
					currentToken = lexer.currentToken();
					lexer.nextToken();

					label = parseLabel(lexer);

					lineHasLabel = true;

				} else {

					lexer.pushTokenBack();
					lexer.pushTokenBack();
					currentToken = lexer.currentToken();
					lexer.nextToken();
				}

				if (currentToken.getType() == Lexer.DOT) {

					directive = parseDirective(lexer);

					currentToken = lexer.currentToken();
					lexer.nextToken();

					lineHasDirective = true;

				} else if (currentToken.getType() == Lexer.IDENTIFIER) {

					instruction = parseInstruction(lexer);

					currentToken = lexer.currentToken();
					lexer.nextToken();

					lineHasInstruction = true;

				}

				if (lineHasLabel) {
					if (lineHasDirective) {
						lineList.appendLine(
							new Line(
								lineList.totalLines() + 1,
								lexer.getOriginalLine(),
								label,
								directive));
					} else if (lineHasInstruction) {
						lineList.appendLine(
							new Line(
								lineList.totalLines() + 1,
								lexer.getOriginalLine(),
								label,
								instruction));
					} else {
						lineList.appendLine(
							new Line(
								lineList.totalLines() + 1,
								lexer.getOriginalLine(),
								label));
					}
				} else {
					if (lineHasDirective) {
						lineList.appendLine(
							new Line(
								lineList.totalLines() + 1,
								lexer.getOriginalLine(),
								directive));
					} else if (lineHasInstruction) {
						lineList.appendLine(
							new Line(
								lineList.totalLines() + 1,
								lexer.getOriginalLine(),
								instruction));
					}
				}
			}
			lineHasLabel = false;
			lineHasInstruction = false;
			lineHasDirective = false;

			match(lexer, Lexer.NEWLINE);

			instructionCount++;
		}
		return lineList;
	}

	private Directive parseDirective(Lexer lexer)
		throws ParseException, LexerException {

		match(lexer, Lexer.DOT);

		if (currentToken.getValue().toLowerCase().equals("asciiz")) {

			return new Directive(
				".asciiz",
				lexer,
				new int[] { Operand.TYPE_STRING });
		} else if (currentToken.getValue().toLowerCase().equals("text")) {
			lexer.mark();

			return new Directive(".text", lexer, new int[] {});

		} else if (currentToken.getValue().toLowerCase().equals("data")) {

			return new Directive(".data", lexer, new int[] {});

		} else if (currentToken.getValue().toLowerCase().equals("globl")) {

			return new Directive(
				".globl",
				lexer,
				new int[] { Operand.TYPE_LABEL });

		} else if (currentToken.getValue().toLowerCase().equals("space")) {
			return new Directive(
				".space",
				lexer,
				new int[] { Operand.TYPE_IMMEDIATE });

		} else {
			throw new ParseException(
				"Unsupported Directive '" + currentToken.getValue() + "'",
				lexer.getLineNumber());
		}
	}

	private Instruction parseInstruction(Lexer lexer)
		throws ParseException, LexerException {
		/*
		 * parse supported instructions, these if-else statements
		 * should be populated by a XSLT transformation of the XML
		 * instruction file.
		 */
		if (currentToken.getValue().toLowerCase().equals("add")) {
			lexer.mark();
			try {
				return new RInstruction(
					"add",
					lexer,
					new int[] {
						Operand.TYPE_REGISTER,
						Operand.TYPE_REGISTER,
						Operand.TYPE_REGISTER });
			} catch (ParseException pe) {
				lexer.rewind();
				return new RInstruction(
					"add",
					lexer,
					new int[] {
						Operand.TYPE_REGISTER,
						Operand.TYPE_REGISTER,
						Operand.TYPE_IMMEDIATE });
					
			}

		} else if (currentToken.getValue().toLowerCase().equals("sub")) {
			lexer.mark();
			try {
				return new RInstruction(
					"sub",
					lexer,
					new int[] {
						Operand.TYPE_REGISTER,
						Operand.TYPE_REGISTER,
						Operand.TYPE_REGISTER });
			} catch (ParseException pe) {
				lexer.rewind();
				return new RInstruction(
					"sub",
					lexer,
					new int[] {
						Operand.TYPE_REGISTER,
						Operand.TYPE_REGISTER,
						Operand.TYPE_IMMEDIATE });
			}
		} else if (currentToken.getValue().toLowerCase().equals("mul")) {
			lexer.mark();
			try {
				return new RInstruction(
					"mul",
					lexer,
					new int[] {
						Operand.TYPE_REGISTER,
						Operand.TYPE_REGISTER,
						Operand.TYPE_REGISTER });
			} catch (ParseException pe) {
				lexer.rewind();
					return new RInstruction(
						"mul",
						lexer,
						new int[] {
							Operand.TYPE_REGISTER,
							Operand.TYPE_REGISTER,
							Operand.TYPE_IMMEDIATE });
				}
		} else if (currentToken.getValue().toLowerCase().equals("div")) {
			lexer.mark();
			try {
				return new RInstruction(
					"div",
					lexer,
					new int[] {
						Operand.TYPE_REGISTER,
						Operand.TYPE_REGISTER,
						Operand.TYPE_REGISTER });
			} catch (ParseException pe) {
				lexer.rewind();
				try {
					return new RInstruction(
						"div",
						lexer,
						new int[] {
							Operand.TYPE_REGISTER,
							Operand.TYPE_REGISTER,
							Operand.TYPE_IMMEDIATE });
				} catch (ParseException pe1) {
					lexer.rewind();
					try {
						return new RInstruction(
							"div",
							lexer,
							new int[] {
								Operand.TYPE_REGISTER,
								Operand.TYPE_REGISTER });
					} catch (ParseException pe2) {
						throw new ParseException(
							"Failed to parse 'div'",
							lineList.totalLines() + 1);
					}
				}
			}
		}else if(currentToken.getValue().toLowerCase().equals("muli")){
			
			return new IInstruction("muli", lexer, new int[]{Operand.TYPE_REGISTER, Operand.TYPE_IMMEDIATE, Operand.TYPE_IMMEDIATE}); 
		
		}else if (currentToken.getValue().toLowerCase().equals("neg")) {

			return new RInstruction(
				"neg",
				lexer,
				new int[] { Operand.TYPE_REGISTER, Operand.TYPE_REGISTER });

		} else if (currentToken.getValue().toLowerCase().equals("b")) {
			return new JInstruction(
				"b",
				lexer,
				new int[] { Operand.TYPE_LABEL });

		} else if (currentToken.getValue().toLowerCase().equals("beq")) {
			lexer.mark();
			try {
				return new JInstruction(
					"beq",
					lexer,
					new int[] {
						Operand.TYPE_REGISTER,
						Operand.TYPE_REGISTER,
						Operand.TYPE_LABEL });
			} catch (ParseException pe1) {
				lexer.rewind();
				return new JInstruction(
					"beq",
					lexer,
					new int[] {
						Operand.TYPE_REGISTER,
						Operand.TYPE_IMMEDIATE,
						Operand.TYPE_LABEL });
			}
		} else if (currentToken.getValue().toLowerCase().equals("beqz")) {
			lexer.mark();
			try {
				return new JInstruction(
					"beqz",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_LABEL });
			} catch (ParseException pe1) {
				lexer.rewind();
				return new JInstruction(
					"beqz",
					lexer,
					new int[] { Operand.TYPE_IMMEDIATE, Operand.TYPE_LABEL });
			}

		} else if (currentToken.getValue().toLowerCase().equals("bne")) {
			lexer.mark();
			try {
				return new JInstruction(
					"bne",
					lexer,
					new int[] {
						Operand.TYPE_REGISTER,
						Operand.TYPE_REGISTER,
						Operand.TYPE_LABEL });
			} catch (ParseException pe1) {
				lexer.rewind();
				return new JInstruction(
					"bne",
					lexer,
					new int[] {
						Operand.TYPE_REGISTER,
						Operand.TYPE_IMMEDIATE,
						Operand.TYPE_LABEL });
			}
		} else if (currentToken.getValue().toLowerCase().equals("bnez")) {
			lexer.mark();
			try {
				return new JInstruction(
					"bnez",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_LABEL });
			} catch (ParseException pe1) {
				lexer.rewind();
				return new JInstruction(
					"bnez",
					lexer,
					new int[] { Operand.TYPE_IMMEDIATE, Operand.TYPE_LABEL });
			}
		} else if (currentToken.getValue().toLowerCase().equals("bgt")) {
			lexer.mark();
			try {
				return new JInstruction(
					"bgt",
					lexer,
					new int[] {
						Operand.TYPE_REGISTER,
						Operand.TYPE_REGISTER,
						Operand.TYPE_LABEL });
			} catch (ParseException pe1) {
				lexer.rewind();
				return new JInstruction(
					"bgt",
					lexer,
					new int[] {
						Operand.TYPE_REGISTER,
						Operand.TYPE_IMMEDIATE,
						Operand.TYPE_LABEL });
			}


		} else if (currentToken.getValue().toLowerCase().equals("bgtz")) {
			lexer.mark();
			try {
				return new JInstruction(
					"bgtz",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_LABEL });
			} catch (ParseException pe1) {
				lexer.rewind();
				return new JInstruction(
					"bgtz",
					lexer,
					new int[] { Operand.TYPE_IMMEDIATE, Operand.TYPE_LABEL });
			}
		} else if (currentToken.getValue().toLowerCase().equals("bge")) {

/*
			lexer.mark();
			try {
			} catch (ParseException pe1) {
				lexer.rewind();
			}
*/
			lexer.mark();
			try {
				return new JInstruction(
					"bge",
					lexer,
					new int[] {
						Operand.TYPE_REGISTER,
						Operand.TYPE_REGISTER,
						Operand.TYPE_LABEL });
				
				
			} catch (ParseException pe1) {
				lexer.rewind();
					return new JInstruction(
						"bge",
					lexer,
					new int[] {
						Operand.TYPE_REGISTER,
						Operand.TYPE_IMMEDIATE,
						Operand.TYPE_LABEL });
				}
		} else if (currentToken.getValue().toLowerCase().equals("bgez")) {
			lexer.mark();
			try {
				return new JInstruction(
					"bgez",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_LABEL });
			} catch (ParseException pe1) {
				lexer.rewind();
				return new JInstruction(
					"bgez",
					lexer,
					new int[] { Operand.TYPE_IMMEDIATE, Operand.TYPE_LABEL });
			}
		} else if (currentToken.getValue().toLowerCase().equals("blt")) {
			lexer.mark();
			try {
				return new JInstruction(
					"blt",
					lexer,
					new int[] {
						Operand.TYPE_REGISTER,
						Operand.TYPE_REGISTER,
						Operand.TYPE_LABEL });
			} catch (ParseException pe1) {
				lexer.rewind();
				return new JInstruction(
					"blt",
					lexer,
					new int[] {
						Operand.TYPE_REGISTER,
						Operand.TYPE_IMMEDIATE,
						Operand.TYPE_LABEL });
			}
		} else if (currentToken.getValue().toLowerCase().equals("bltz")) {
			lexer.mark();
			try {
				return new JInstruction(
					"bltz",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_LABEL });
			} catch (ParseException pe1) {
				lexer.rewind();
				return new JInstruction(
					"bltz",
					lexer,
					new int[] { Operand.TYPE_IMMEDIATE, Operand.TYPE_LABEL });
			}
		} else if (currentToken.getValue().toLowerCase().equals("ble")) {
			lexer.mark();
			try {
				return new JInstruction(
					"ble",
					lexer,
					new int[] {
						Operand.TYPE_REGISTER,
						Operand.TYPE_REGISTER,
						Operand.TYPE_LABEL });
			} catch (ParseException pe1) {
				lexer.rewind();
				return new JInstruction(
					"ble",
					lexer,
					new int[] {
						Operand.TYPE_REGISTER,
						Operand.TYPE_IMMEDIATE,
						Operand.TYPE_LABEL });
			}
		} else if (currentToken.getValue().toLowerCase().equals("blez")) {
			lexer.mark();
			try {
				return new JInstruction(
					"blez",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_LABEL });
			} catch (ParseException pe1) {
				lexer.rewind();
				return new JInstruction(
					"blez",
					lexer,
					new int[] { Operand.TYPE_IMMEDIATE, Operand.TYPE_LABEL });
			}
		} else if (currentToken.getValue().toLowerCase().equals("li")) {

			return new IInstruction(
				"li",
				lexer,
				new int[] { Operand.TYPE_REGISTER, Operand.TYPE_IMMEDIATE });

		} else if (currentToken.getValue().toLowerCase().equals("lui")) {

			return new IInstruction(
				"lui",
				lexer,
				new int[] { Operand.TYPE_REGISTER, Operand.TYPE_IMMEDIATE });

		} else if (currentToken.getValue().toLowerCase().equals("la")) {

			return new IInstruction(
				"la",
				lexer,
				new int[] { Operand.TYPE_REGISTER, Operand.TYPE_ADDRESSING });

		} else if (currentToken.getValue().toLowerCase().equals("lw")) {

			return new IInstruction(
				"lw",
				lexer,
				new int[] { Operand.TYPE_REGISTER, Operand.TYPE_ADDRESSING });

		} else if (currentToken.getValue().toLowerCase().equals("sw")) {

			return new IInstruction(
				"sw",
				lexer,
				new int[] { Operand.TYPE_REGISTER, Operand.TYPE_ADDRESSING });

		} else if (currentToken.getValue().toLowerCase().equals("move")) {

			return new RInstruction(
				"move",
				lexer,
				new int[] { Operand.TYPE_REGISTER, Operand.TYPE_REGISTER });

		} else if (currentToken.getValue().toLowerCase().equals("j")) {

			return new JInstruction(
				"j",
				lexer,
				new int[] { Operand.TYPE_LABEL });

			//		} else if (currentToken.getValue().toLowerCase().equals("jal")) {
			//
			//					return new JInstruction(
			//						"b",
			//						lexer,
			//						new int[] { Operand.TYPE_LABEL });

		} else if (currentToken.getValue().toLowerCase().equals("syscall")) {

			return new RInstruction("syscall", lexer, new int[] {});

		} else {
			throw new ParseException(
				"Unsupported Instruction '" + currentToken.getValue() + "'",
				lineList.totalLines() + 1);
		}
	}

	private Label parseLabel(Lexer lexer)
		throws LexerException, ParseException {
		String label = new String(currentToken.getValue());
		match(lexer, Lexer.IDENTIFIER);
		match(lexer, Lexer.COLON);
		return new Label(label);
	}

	/**
	 * Parses the program given by the argument.
	 * 
	 * @param program the program input.
	 * @return a collection of the <code>Line</code> that represents the user's input program.
	 * @throws LexerException if the lexer finds an error
	 * @throws ParseException if the parser finds an error
	 */
	public LineList parse(StringBuffer program)
		throws LexerException, ParseException {
		this.program = program;
		return parse();
	}
}
