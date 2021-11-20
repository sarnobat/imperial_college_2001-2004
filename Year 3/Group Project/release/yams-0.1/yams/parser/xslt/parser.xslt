<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text" version="1.0" encoding="UTF-8" indent="yes"/>

<xsl:template match="Root">
	package yams.parser;

	/**
	 * Auto generated from the XML instruction file.
	 */
	public class Parser{

		private Token currentToken;
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
				
					lineList.appendLine(new Line(lineList.totalLines() + 1));

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

					} else if(currentToken.getType() == Lexer.IDENTIFIER){

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
									label,
									directive));
						} else if (lineHasInstruction) {
							lineList.appendLine(
								new Line(
									lineList.totalLines() + 1,
									label,
									instruction));
						} else {
							lineList.appendLine(
								new Line(lineList.totalLines() + 1, label));
						}
					} else {
						if (lineHasDirective) {
							lineList.appendLine(
								new Line(lineList.totalLines() + 1, directive));
						} else if (lineHasInstruction) {
							lineList.appendLine(
								new Line(lineList.totalLines() + 1, instruction));
						}
					}
				}
				lineHasLabel = false;
				lineHasInstruction = false;
				lineHasDirective = false;

				match(lexer, Lexer.NEWLINE);
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

			return new RInstruction(
				"add",
				lexer,
				new int[] {
					Operand.TYPE_REGISTER,
					Operand.TYPE_REGISTER,
					Operand.TYPE_REGISTER });

		} else if (currentToken.getValue().toLowerCase().equals("sub")) {
			return new RInstruction(
				"sub",
				lexer,
				new int[] {
					Operand.TYPE_REGISTER,
					Operand.TYPE_REGISTER,
					Operand.TYPE_REGISTER });

		} else if (currentToken.getValue().toLowerCase().equals("mul")) {

			return new RInstruction(
				"mul",
				lexer,
				new int[] {
					Operand.TYPE_REGISTER,
					Operand.TYPE_REGISTER,
					Operand.TYPE_REGISTER });

		} else if (currentToken.getValue().toLowerCase().equals("div")) {
			return new RInstruction(
				"div",
				lexer,
				new int[] {
					Operand.TYPE_REGISTER,
					Operand.TYPE_REGISTER,
					Operand.TYPE_REGISTER });
		} else if (currentToken.getValue().toLowerCase().equals("neg")) {

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

			return new JInstruction(
				"beq",
				lexer,
				new int[] {
					Operand.TYPE_REGISTER,
					Operand.TYPE_REGISTER,
					Operand.TYPE_LABEL });

		} else if (currentToken.getValue().toLowerCase().equals("beqz")) {

			return new JInstruction(
				"beqz",
				lexer,
				new int[] { Operand.TYPE_REGISTER, Operand.TYPE_LABEL });

		} else if (currentToken.getValue().toLowerCase().equals("bne")) {

			return new JInstruction(
				"bne",
				lexer,
				new int[] {
					Operand.TYPE_REGISTER,
					Operand.TYPE_REGISTER,
					Operand.TYPE_LABEL });

		} else if (currentToken.getValue().toLowerCase().equals("bnez")) {

			return new JInstruction(
				"bnez",
				lexer,
				new int[] { Operand.TYPE_REGISTER, Operand.TYPE_LABEL });

		} else if (currentToken.getValue().toLowerCase().equals("bgt")) {

			return new JInstruction(
				"bgt",
				lexer,
				new int[] {
					Operand.TYPE_REGISTER,
					Operand.TYPE_REGISTER,
					Operand.TYPE_LABEL });

		} else if (currentToken.getValue().toLowerCase().equals("bgtz")) {

			return new JInstruction(
				"bgtz",
				lexer,
				new int[] { Operand.TYPE_REGISTER, Operand.TYPE_LABEL });

		} else if (currentToken.getValue().toLowerCase().equals("bge")) {

			return new JInstruction(
				"bge",
				lexer,
				new int[] {
					Operand.TYPE_REGISTER,
					Operand.TYPE_REGISTER,
					Operand.TYPE_LABEL });

		} else if (currentToken.getValue().toLowerCase().equals("bgez")) {

			return new JInstruction(
				"bgez",
				lexer,
				new int[] { Operand.TYPE_REGISTER, Operand.TYPE_LABEL });

		} else if (currentToken.getValue().toLowerCase().equals("blt")) {

			return new JInstruction(
				"blt",
				lexer,
				new int[] {
					Operand.TYPE_REGISTER,
					Operand.TYPE_REGISTER,
					Operand.TYPE_LABEL });

		} else if (currentToken.getValue().toLowerCase().equals("bltz")) {

			return new JInstruction(
				"bltz",
				lexer,
				new int[] { Operand.TYPE_REGISTER, Operand.TYPE_LABEL });

		} else if (currentToken.getValue().toLowerCase().equals("ble")) {

			return new JInstruction(
				"ble",
				lexer,
				new int[] {
					Operand.TYPE_REGISTER,
					Operand.TYPE_REGISTER,
					Operand.TYPE_LABEL });

		} else if (currentToken.getValue().toLowerCase().equals("blez")) {

			return new JInstruction(
				"blez",
				lexer,
				new int[] { Operand.TYPE_REGISTER, Operand.TYPE_LABEL });

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
	
	<xsl:apply-templates select="Instructions"/>
}
</xsl:template>

<xsl:template match="Instructions">
	in Instructions
	<xsl:apply-templates select="Instruction"/>
</xsl:template>

<xsl:template match="Instruction">
if (currentToken.getValue().toLowerCase().equals("<xsl:value-of select="Name">")) {

	return new <xsl:value-of select="Type">Instruction(
		"<xsl:value-of select="Name">",
		lexer,
		new int[] {
			Operand.TYPE_REGISTER,
			Operand.TYPE_REGISTER,
			Operand.TYPE_REGISTER });

}
</xsl:template>

<xsl:template match="Operand">
	Operand.TYPE_<xsl:value-of select="."/>
</xsl:template>

</xsl:stylesheet>
