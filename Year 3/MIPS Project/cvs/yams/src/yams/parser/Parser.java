
/*
 * Created on 2003-11-1
 */
package yams.parser;

//Generated version.

/**
 * Parses the users file. Invokes the Lexer automatically.
 * 
 * @author Qian Qiao qq01@doc.ic.ac.uk
 */
public class Parser {
private Token currentToken;

	private int instructionCount = 0;

	private LineList lineList;
	private StringBuffer program;

	/**
	 * Constructs a new parser. If a parser is constructed this way, then the method
	 * parse(StringBuffer Program) should be used.
	 */
	public Parser() {
		lineList = new LineList();
	}

	/**
	 * Constructs a parser that parses the program.
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
	 * @return a collection of the Line that represents the user's input program.
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

	
			if (currentToken.getValue().toLowerCase().equals("add")) {
			lexer.mark();
			
	lexer.rewind();
	try{
		return new RInstruction(
					"add",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_REGISTER, Operand.TYPE_REGISTER });
	
	}catch(ParseException pe2){
	
	lexer.rewind();
	try{
		return new RInstruction(
					"add",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_REGISTER, Operand.TYPE_IMMEDIATE });
	
	}catch(ParseException pe1){
	
		throw new ParseException(
			"Failed to parse 'add'",
			lineList.totalLines() + 1);
	
	}

	}

			}
		
			if (currentToken.getValue().toLowerCase().equals("addu")) {

				return new RInstruction(
					"addu",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_REGISTER, Operand.TYPE_REGISTER });

			}
		
			if (currentToken.getValue().toLowerCase().equals("addi")) {

				return new RInstruction(
					"addi",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_REGISTER, Operand.TYPE_IMMEDIATE });

			}
		
			if (currentToken.getValue().toLowerCase().equals("sub")) {
			lexer.mark();
			
	lexer.rewind();
	try{
		return new RInstruction(
					"sub",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_REGISTER, Operand.TYPE_REGISTER });
	
	}catch(ParseException pe2){
	
	lexer.rewind();
	try{
		return new RInstruction(
					"sub",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_REGISTER, Operand.TYPE_IMMEDIATE });
	
	}catch(ParseException pe1){
	
		throw new ParseException(
			"Failed to parse 'sub'",
			lineList.totalLines() + 1);
	
	}

	}

			}
		
			if (currentToken.getValue().toLowerCase().equals("mul")) {
			lexer.mark();
			
	lexer.rewind();
	try{
		return new RInstruction(
					"mul",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_REGISTER, Operand.TYPE_REGISTER });
	
	}catch(ParseException pe2){
	
	lexer.rewind();
	try{
		return new RInstruction(
					"mul",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_REGISTER, Operand.TYPE_IMMEDIATE });
	
	}catch(ParseException pe1){
	
		throw new ParseException(
			"Failed to parse 'mul'",
			lineList.totalLines() + 1);
	
	}

	}

			}
		
			if (currentToken.getValue().toLowerCase().equals("div")) {
			lexer.mark();
			
	lexer.rewind();
	try{
		return new RInstruction(
					"div",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_REGISTER, Operand.TYPE_REGISTER });
	
	}catch(ParseException pe3){
	
	lexer.rewind();
	try{
		return new RInstruction(
					"div",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_REGISTER, Operand.TYPE_IMMEDIATE });
	
	}catch(ParseException pe2){
	
	lexer.rewind();
	try{
		return new RInstruction(
					"div",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_REGISTER });
	
	}catch(ParseException pe1){
	
		throw new ParseException(
			"Failed to parse 'div'",
			lineList.totalLines() + 1);
	
	}

	}

	}

			}
		
			if (currentToken.getValue().toLowerCase().equals("neg")) {

				return new RInstruction(
					"neg",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_REGISTER });

			}
		
			if (currentToken.getValue().toLowerCase().equals("lui")) {

				return new RInstruction(
					"lui",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_IMMEDIATE });

			}
		
			if (currentToken.getValue().toLowerCase().equals("li")) {

				return new RInstruction(
					"li",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_IMMEDIATE });

			}
		
			if (currentToken.getValue().toLowerCase().equals("b")) {

				return new RInstruction(
					"b",
					lexer,
					new int[] { Operand.TYPE_LABEL });

			}
		
			if (currentToken.getValue().toLowerCase().equals("beq")) {
			lexer.mark();
			
	lexer.rewind();
	try{
		return new RInstruction(
					"beq",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_REGISTER, Operand.TYPE_LABEL });
	
	}catch(ParseException pe2){
	
	lexer.rewind();
	try{
		return new RInstruction(
					"beq",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_IMMEDIATE, Operand.TYPE_LABEL });
	
	}catch(ParseException pe1){
	
		throw new ParseException(
			"Failed to parse 'beq'",
			lineList.totalLines() + 1);
	
	}

	}

			}
		
			if (currentToken.getValue().toLowerCase().equals("bgez")) {
			lexer.mark();
			
	lexer.rewind();
	try{
		return new RInstruction(
					"bgez",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_LABEL });
	
	}catch(ParseException pe2){
	
	lexer.rewind();
	try{
		return new RInstruction(
					"bgez",
					lexer,
					new int[] { Operand.TYPE_IMMEDIATE, Operand.TYPE_LABEL });
	
	}catch(ParseException pe1){
	
		throw new ParseException(
			"Failed to parse 'bgez'",
			lineList.totalLines() + 1);
	
	}

	}

			}
		
			if (currentToken.getValue().toLowerCase().equals("bgtz")) {
			lexer.mark();
			
	lexer.rewind();
	try{
		return new RInstruction(
					"bgtz",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_LABEL });
	
	}catch(ParseException pe2){
	
	lexer.rewind();
	try{
		return new RInstruction(
					"bgtz",
					lexer,
					new int[] { Operand.TYPE_IMMEDIATE, Operand.TYPE_LABEL });
	
	}catch(ParseException pe1){
	
		throw new ParseException(
			"Failed to parse 'bgtz'",
			lineList.totalLines() + 1);
	
	}

	}

			}
		
			if (currentToken.getValue().toLowerCase().equals("blez")) {
			lexer.mark();
			
	lexer.rewind();
	try{
		return new RInstruction(
					"blez",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_LABEL });
	
	}catch(ParseException pe2){
	
	lexer.rewind();
	try{
		return new RInstruction(
					"blez",
					lexer,
					new int[] { Operand.TYPE_IMMEDIATE, Operand.TYPE_LABEL });
	
	}catch(ParseException pe1){
	
		throw new ParseException(
			"Failed to parse 'blez'",
			lineList.totalLines() + 1);
	
	}

	}

			}
		
			if (currentToken.getValue().toLowerCase().equals("bltz")) {
			lexer.mark();
			
	lexer.rewind();
	try{
		return new RInstruction(
					"bltz",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_LABEL });
	
	}catch(ParseException pe2){
	
	lexer.rewind();
	try{
		return new RInstruction(
					"bltz",
					lexer,
					new int[] { Operand.TYPE_IMMEDIATE, Operand.TYPE_LABEL });
	
	}catch(ParseException pe1){
	
		throw new ParseException(
			"Failed to parse 'bltz'",
			lineList.totalLines() + 1);
	
	}

	}

			}
		
			if (currentToken.getValue().toLowerCase().equals("bne")) {
			lexer.mark();
			
	lexer.rewind();
	try{
		return new RInstruction(
					"bne",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_REGISTER, Operand.TYPE_LABEL });
	
	}catch(ParseException pe2){
	
	lexer.rewind();
	try{
		return new RInstruction(
					"bne",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_IMMEDIATE, Operand.TYPE_LABEL });
	
	}catch(ParseException pe1){
	
		throw new ParseException(
			"Failed to parse 'bne'",
			lineList.totalLines() + 1);
	
	}

	}

			}
		
			if (currentToken.getValue().toLowerCase().equals("beqz")) {
			lexer.mark();
			
	lexer.rewind();
	try{
		return new RInstruction(
					"beqz",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_LABEL });
	
	}catch(ParseException pe2){
	
	lexer.rewind();
	try{
		return new RInstruction(
					"beqz",
					lexer,
					new int[] { Operand.TYPE_IMMEDIATE, Operand.TYPE_LABEL });
	
	}catch(ParseException pe1){
	
		throw new ParseException(
			"Failed to parse 'beqz'",
			lineList.totalLines() + 1);
	
	}

	}

			}
		
			if (currentToken.getValue().toLowerCase().equals("bge")) {
			lexer.mark();
			
	lexer.rewind();
	try{
		return new RInstruction(
					"bge",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_REGISTER, Operand.TYPE_LABEL });
	
	}catch(ParseException pe2){
	
	lexer.rewind();
	try{
		return new RInstruction(
					"bge",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_IMMEDIATE, Operand.TYPE_LABEL });
	
	}catch(ParseException pe1){
	
		throw new ParseException(
			"Failed to parse 'bge'",
			lineList.totalLines() + 1);
	
	}

	}

			}
		
			if (currentToken.getValue().toLowerCase().equals("bgt")) {
			lexer.mark();
			
	lexer.rewind();
	try{
		return new RInstruction(
					"bgt",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_REGISTER, Operand.TYPE_LABEL });
	
	}catch(ParseException pe2){
	
	lexer.rewind();
	try{
		return new RInstruction(
					"bgt",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_IMMEDIATE, Operand.TYPE_LABEL });
	
	}catch(ParseException pe1){
	
		throw new ParseException(
			"Failed to parse 'bgt'",
			lineList.totalLines() + 1);
	
	}

	}

			}
		
			if (currentToken.getValue().toLowerCase().equals("ble")) {
			lexer.mark();
			
	lexer.rewind();
	try{
		return new RInstruction(
					"ble",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_REGISTER, Operand.TYPE_LABEL });
	
	}catch(ParseException pe2){
	
	lexer.rewind();
	try{
		return new RInstruction(
					"ble",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_IMMEDIATE, Operand.TYPE_LABEL });
	
	}catch(ParseException pe1){
	
		throw new ParseException(
			"Failed to parse 'ble'",
			lineList.totalLines() + 1);
	
	}

	}

			}
		
			if (currentToken.getValue().toLowerCase().equals("blt")) {
			lexer.mark();
			
	lexer.rewind();
	try{
		return new RInstruction(
					"blt",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_REGISTER, Operand.TYPE_LABEL });
	
	}catch(ParseException pe2){
	
	lexer.rewind();
	try{
		return new RInstruction(
					"blt",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_IMMEDIATE, Operand.TYPE_LABEL });
	
	}catch(ParseException pe1){
	
		throw new ParseException(
			"Failed to parse 'blt'",
			lineList.totalLines() + 1);
	
	}

	}

			}
		
			if (currentToken.getValue().toLowerCase().equals("bnez")) {
			lexer.mark();
			
	lexer.rewind();
	try{
		return new RInstruction(
					"bnez",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_LABEL });
	
	}catch(ParseException pe2){
	
	lexer.rewind();
	try{
		return new RInstruction(
					"bnez",
					lexer,
					new int[] { Operand.TYPE_IMMEDIATE, Operand.TYPE_LABEL });
	
	}catch(ParseException pe1){
	
		throw new ParseException(
			"Failed to parse 'bnez'",
			lineList.totalLines() + 1);
	
	}

	}

			}
		
			if (currentToken.getValue().toLowerCase().equals("j")) {

				return new RInstruction(
					"j",
					lexer,
					new int[] { Operand.TYPE_LABEL });

			}
		
			if (currentToken.getValue().toLowerCase().equals("jal")) {

				return new RInstruction(
					"jal",
					lexer,
					new int[] {  });

			}
		
			if (currentToken.getValue().toLowerCase().equals("syscall")) {

				return new RInstruction(
					"syscall",
					lexer,
					new int[] {  });

			}
		
			if (currentToken.getValue().toLowerCase().equals("la")) {

				return new RInstruction(
					"la",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_ADDRESSING });

			}
		
			if (currentToken.getValue().toLowerCase().equals("lw")) {

				return new RInstruction(
					"lw",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_ADDRESSING });

			}
		
			if (currentToken.getValue().toLowerCase().equals("sw")) {

				return new RInstruction(
					"sw",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_ADDRESSING });

			}
		
			if (currentToken.getValue().toLowerCase().equals("move")) {

				return new RInstruction(
					"move",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_REGISTER });

			}
		
			if (currentToken.getValue().toLowerCase().equals("mflo")) {

				return new RInstruction(
					"mflo",
					lexer,
					new int[] {  });

			}
		
			if (currentToken.getValue().toLowerCase().equals("ori")) {

				return new RInstruction(
					"ori",
					lexer,
					new int[] {  });

			}
		
			if (currentToken.getValue().toLowerCase().equals("muli")) {

				return new RInstruction(
					"muli",
					lexer,
					new int[] { Operand.TYPE_REGISTER, Operand.TYPE_IMMEDIATE, Operand.TYPE_IMMEDIATE });

			}
		
	
		else{
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
	 * @return a collection of the Line that represents the user's input program.
	 * @throws LexerException if the lexer finds an error
	 * @throws ParseException if the parser finds an error
	 */
	public LineList parse(StringBuffer program)
		throws LexerException, ParseException {
		this.program = program;
		return parse();
	}
	
}
