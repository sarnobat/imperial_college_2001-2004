<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="text" version="1.0" encoding="UTF-8" indent="yes"/>

<xsl:template match="Instructions">
/*
 * Created on 2003-11-1
 */
package yams.parser;

//Generated version.

/**
 * Parses the users file. Invokes the <code>Lexer</code> automatically.
 * 
 * @author Qian Qiao <a href="qq01@doc.ic.ac.uk">qq01@doc.ic.ac.uk</a>
 */
public class Parser {
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

	<xsl:apply-templates select="Instruction"/>
	
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
</xsl:template>

<xsl:template match="Instruction">
	<xsl:variable name="opTypeCount" select="count(OperandTypes)"/>
	<xsl:choose>
		<xsl:when test="count(OperandTypes) = 1">
			if (currentToken.getValue().toLowerCase().equals("<xsl:value-of select="Name"/>")) {

				return new RInstruction(
					"<xsl:value-of select="Name"/>",
					lexer,
					new int[] { <xsl:value-of select="OperandTypes"/> });

			}
		</xsl:when>
		
		<xsl:otherwise>
			if (currentToken.getValue().toLowerCase().equals("<xsl:value-of select="Name"/>")) {
			lexer.mark();
			<xsl:variable name="i" select="$opTypeCount"/>
			<xsl:call-template name="non-determ-instr">
				<xsl:with-param name="i" select="$opTypeCount"/>
				<xsl:with-param name="totalTypes" select="$opTypeCount"/>
			</xsl:call-template>
			}
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template name="non-determ-instr">
<xsl:param name="i"/>
<xsl:param name="totalTypes"/>
	lexer.rewind();
	try{
		return new RInstruction(
					"<xsl:value-of select="Name"/>",
					lexer,
					new int[] { <xsl:value-of select="OperandTypes[$totalTypes - $i + 1]"/> });
	
	}catch(ParseException pe<xsl:value-of select="$i"/>){
	<xsl:if test="$i &gt; 1">
	<xsl:call-template name="non-determ-instr">
		<xsl:with-param name="i" select="$i - 1"/>
		<xsl:with-param name="totalTypes" select="$totalTypes"/>
	</xsl:call-template>
	</xsl:if>

	<xsl:if test="$i = 1">
		throw new ParseException(
			"Failed to parse '<xsl:value-of select="Name"/>'",
			lineList.totalLines() + 1);
	</xsl:if>
	}
</xsl:template>
	
</xsl:stylesheet>
