import antlr.CommonAST;
import antlr.DumpASTVisitor;
import java.io.*;
import java.util.*;

public class textTranslator
{//All methods will have to be
	private  static MAST ast;
 	private  static PrintStream codeout;
	private  static int nextLabel;
	private  static boolean inverted = true;
	private  static boolean notInverted = false;

	public static void translateText(MAST tree,PrintStream file)
	{
		ast = tree;
		codeout = file;
		nextLabel = -1;

		if (ast.getType() == Decm2LexerTokenTypes.MODULE)
		{
			translateStatList((MAST) ast.getFirstChild().getNextSibling(),0);
		}
	}

	public static String transReg(int virtReg)
	{//input will be a number
		/*String reg = Regs[nextFree];
		nextFree++;
		return reg;*/

		String reg = "$";

		if (0 <= virtReg && virtReg <= 9)
		{
			reg += "t" + virtReg;
		}
		else
		{
			switch(virtReg)
			{
				case 10:	{reg ="s0";break;}
				case 11:	{reg +="s1";break;}
				case 12:	{reg +="s2";break;}
				case 13:	{reg +="s3";break;}
				case 14:	{reg +="s4";break;}
				case 15:	{reg +="s5";break;}
				case 16:	{reg +="s6";break;}
				case 17:	{reg +="s7";break;}
				case 18:	{reg +="a0";break;}
				case 19:	{reg +="a1";break;}
				case 20:	{reg +="a2";break;}
				case 21:	{reg +="a3";break;}
			}
		}
		return reg;
	}

	public static void translateStatList(MAST ast,int firstFreeReg)
	{//all registers before firstFreeReg have been used to store a value
		if (ast == null)
		{
			return;
		}

		if (ast.getType() == Decm2LexerTokenTypes.BEGIN)
		{
			MAST firstStat = (MAST) ast.getFirstChild();
			while (firstStat != null)
			{
				translateStat(firstStat,firstFreeReg);
				firstStat = (MAST) firstStat.getNextSibling();
			}
		}
	}

	public static void translateStat(MAST ast,int firstFreeReg)
	{
		if (ast != null)
		{
			switch(ast.getType())
			{
				case (Decm2LexerTokenTypes.ASSIGN):
				{
					translateAssignStat(ast);
					break;
				}
				case (Decm2LexerTokenTypes.CALL):
				{
					translateFunCall(ast);
					break;
				}
				case (Decm2LexerTokenTypes.IF):
				{

					translateIf(ast);
					break;
				}
				case (Decm2LexerTokenTypes.WHILE):
				{
					translateWhile(ast);
					break;
				}
				case (Decm2LexerTokenTypes.REPEAT):
				{
					translateRepeat(ast);
					break;
				}
			}
		}
	}

	public static void translateAssignStat(MAST ast,int firstFreeReg)
	{
		MAST rhs = (MAST) ast.getFirstChild().getNextSibling();
		MAST lhs = (MAST) ast.getFirstChild();
		String common = "\t" + transReg(firstFreeReg) + ",";

		if (isAtomic(rhs))
		{
			codeout.println("\tli" + common + rhs.getText());
		}
		else
		{
			translateExpr(rhs,firstFreeReg);
		}
		codeout.println("\tsw" + common + "_" + lhs.getText());

	}

	private static boolean isAtomic(MAST ast)
	{// atomic expressions: ID, NUMBER

		return (ast.getType() == Decm2LexerTokenTypes.ID ||
			ast.getType() == Decm2LexerTokenTypes.NUMBER);
	}
	private static void translateExpr(MAST ast,int firstFreeReg)
	{//pre: ast root node is an operator
		if (isAtomic(ast))
		{
			String opStr = ast.getText();
			if(ast.getType() == Decm2LexerTokenTypes.ID)
			{
				opStr = "_" + opStr;
				codeout.print("\tlw");
			}
			else
			{
				codeout.print("\tli");
			}
			codeout.println("\t$t" + (firstFreeReg) +"," + opStr);
		}
		else
		{
			translateCompExpr(ast,firstFreeReg);
		}

	}
	private static void translateCompExpr(MAST ast,int firstFreeReg)
	{// Root node of ast will be an operator
	//IF BOTH OPERANDS ARE CONSTANTS EVALUATE IMMEDIATELY

		MAST op1 = (MAST) ast.getFirstChild();
		translateExpr(op1,firstFreeReg+1);

		MAST op2 = (MAST) op1.getNextSibling();
		translateExpr(op2,firstFreeReg+2);

		getCorrectMath(ast);
		codeout.println("\t" + transReg(firstFreeReg) + "," + transReg(firstFreeReg+1) + "," + transReg(firstFreeReg+2));

	}


	private static void getCorrectMath(MAST ast)
	{
		codeout.print("\t");
		String symbol = ast.getText();
		if (symbol.equals("+"))
		{
			codeout.print("add");
		}
		if (symbol.equals("-"))
		{
			codeout.print("sub");
		}
		if (symbol.equals("*"))
		{
			codeout.print("mul");
		}
		if (symbol.equals("/"))
		{
			codeout.print("div");
		}
	}

	public static void translateFunCall(MAST ast,int firstFreeReg)
	{
		int callFuncNum;
		MAST funcName = (MAST) ast.getFirstChild();

		if ((funcName.getText()).equals("PutInteger"))
		{//integer must be stored in $a0
			translateExpr((MAST)funcName.getNextSibling(),firstFreeReg);
			codeout.println("\tmove\t$a0," + transReg(firstFreeReg));
			callFuncNum = 1;
		}
		else if ((funcName.getText()).equals("PutLine"))
		{//$a0 must contain the ADDRESS of the string
			 codeout.println("la\t$a0,__newline");
			 callFuncNum = 4;
		}
		else
		{
			return;
			//Invalid function call
		}
		codeout.println("\tli\t$v0," + callFuncNum);
        	codeout.println("\tsyscall");
	}

	public static void translateIf(MAST ast,int firstFreeReg)
	{
		MAST condRoot = (MAST)ast.getFirstChild();
		MAST successStats = (MAST)condRoot.getNextSibling();
		MAST failStats = (MAST)successStats.getNextSibling();

		String successLabel = "L" + newLabel();
		String failLabel = "L" + newLabel();

		translateCond(condRoot,firstFreeReg,successLabel,failLabel);



		codeout.println(successLabel+":");
codeout.println("Success Stats go here");
		translateStatList(successStats,firstFreeReg);
		if(failStats != null)
		{
			String endLabel = "L" + newLabel();
			codeout.println("\tb\t"+endLabel);
			codeout.println(failLabel+":");
codeout.println("Fail Stats go here");
			translateStatList(failStats,firstFreeReg);
			codeout.println(endLabel + ":");
		}
		else
		{
			codeout.println(failLabel+":");
		}
	}

	public static void translateCond(MAST cond,int firstFreeReg,String successLabel,String failLabel)
	{
//System.out.println("Inside translateCond");
		//System.out.println(cond.getText());
		//System.out.println(cond.getType());

		//String nextCondLabel = "L" + newLabel();
		switch(cond.getType())
		{
			case(Decm2LexerTokenTypes.BOOLOP):
			{//only print where to branch if successful test
				/* translate atomic condition	*/

				MAST lhs = (MAST) cond.getFirstChild();
				translateExpr(lhs,firstFreeReg);

				MAST rhs = (MAST) lhs.getNextSibling();
				translateExpr(rhs,firstFreeReg+1);

				String op = cond.getText();
				getCorrectBranch(op);

				codeout.println("\t" + transReg(firstFreeReg) + "," + transReg(firstFreeReg+1) + "," +successLabel);

				codeout.println("\tb\t"+failLabel);

				break;
			}
			case(Decm2LexerTokenTypes.NOT):
			{
				/*	translate inversion */
				MAST invertedCond = (MAST)cond.getFirstChild();
				//System.out.println("Inside NOT case");
				translateCond(invertedCond,firstFreeReg,failLabel,successLabel);

				break;
			}
			case(Decm2LexerTokenTypes.AND):
			{
				/*	translate conjunction */
				MAST firstCond = (MAST) cond.getFirstChild();
				MAST secondCond = (MAST) firstCond.getNextSibling();
				String nextCond = "L" + newLabel();
				String endAndTest = "L" + newLabel();

				translateCond(firstCond,firstFreeReg,nextCond,failLabel);

				codeout.println("\tb\t"+endAndTest);
				codeout.println(nextCond+":");

				if (secondCond.getType() == Decm2LexerTokenTypes.BOOLOP)
				{
					translateCond(secondCond,firstFreeReg,successLabel,failLabel);
				}
				else
				{
					String nextCond2 = "L" + newLabel();
					translateCond(secondCond,firstFreeReg,nextCond2,failLabel);
				}

				codeout.println(endAndTest+":");

				codeout.println("\tb\t"+failLabel);
				break;
			}
			case(Decm2LexerTokenTypes.OR):
			{
				/*	translate disjuntion */
//System.out.println("Inside OR case");
				MAST firstCond = (MAST) cond.getFirstChild();

				MAST secondCond = (MAST) firstCond.getNextSibling();

				translateCond(firstCond,firstFreeReg,successLabel,failLabel);
				translateCond(secondCond,firstFreeReg,successLabel,failLabel);

				codeout.println("\tb\t"+failLabel);

				break;
			}
		}

		//System.out.println("Finished translateCond");

	}

	private static void getCorrectBranch(String op)
	{
		codeout.print("\t");
		if(op.equals("<"))
		{
			codeout.print("blt");
		}
		else if(op.equals("<="))
		{
			codeout.print("ble");
		}
		else if(op.equals("="))
		{
			codeout.print("beq");
		}
		else if(op.equals("#"))
		{
			codeout.print("bne");
		}
		else if(op.equals(">="))
		{
			codeout.print("bge");
		}
		else if(op.equals(">"))
		{
			codeout.print("bg");
		}
	}

	private static int newLabel()
	{
		nextLabel++;
		return nextLabel;
	}

}
