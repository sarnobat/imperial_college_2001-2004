import antlr.CommonAST;
import antlr.DumpASTVisitor;
import java.io.*;
import java.util.*;

public class textTranslator
{//All methods will have to be static
	private static MAST ast;
 	private static PrintStream codeout;
	private static int nextLabel;
	private static boolean inverted = true;
	private static boolean notInverted = false;
	private static CodeGen gen;

	public static void translateText(MAST tree,PrintStream file,CodeGen c)
	{
		ast = tree;
		codeout = file;
		nextLabel = -1;
		gen = c;

		if (ast.getType() == Decm2LexerTokenTypes.MODULE)
		{
			translateStatList((MAST) ast.getFirstChild().getNextSibling(),0);
		}
	}

	public static void translateStatList(MAST ast,int firstFreeReg)
	{
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
					translateAssignStat(ast,firstFreeReg);
					break;
				}
				case (Decm2LexerTokenTypes.CALL):
				{
					translateFunCall(ast,firstFreeReg);
					break;
				}
				case (Decm2LexerTokenTypes.IF):
				{

					translateIf(ast,firstFreeReg);
					break;
				}
				case (Decm2LexerTokenTypes.WHILE):
				{
					translateWhile(ast,firstFreeReg);
					break;
				}
				case (Decm2LexerTokenTypes.REPEAT):
				{
					translateRepeat(ast,firstFreeReg);
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
			codeout.println("\t" + transReg(firstFreeReg) +"," + opStr);
		}

		else
		{
			translateCompExpr(ast,firstFreeReg);
		}

	}


	private static void translateCompExpr(MAST ast,int firstFreeReg)
	{// Root node of ast will be an operator

		if ((ast.getType() == Decm2LexerTokenTypes.ADDOP) && (ast.getFirstChild().getNextSibling() == null))
		{
			MAST subExp = (MAST)ast.getFirstChild();
			if ((ast.getText()).equals("+"))
			{
				translateExpr(subExp,firstFreeReg);
			}
			else if ((ast.getText()).equals("-"))
			{
				translateExpr(subExp,firstFreeReg+1);
				codeout.println("\tneg\t" + transReg(firstFreeReg) + "," + transReg(firstFreeReg+1));
			}
		}
		else
		{
			MAST op1 = (MAST) ast.getFirstChild();
			MAST op2 = (MAST) op1.getNextSibling();

			if (op1.getType() == 33 && op2.getType() == 33)
			{//OPTIMIZATION - both operands are numbers
				String symbol = ast.getText();
				int op1Int = Integer.parseInt(op1.getText());
				int op2Int = Integer.parseInt(op2.getText());
				int ans=0;
				if (symbol.equals("+"))
				{
					ans = op1Int+op2Int;
				}
				if (symbol.equals("-"))
				{
					ans = op1Int-op2Int;
				}
				if (symbol.equals("*"))
				{
					ans = op1Int*op2Int;
				}
				if (symbol.equals("/"))
				{
					ans = op1Int/op2Int;
				}

				codeout.println("\tli\t" + transReg(firstFreeReg) + "," + ans);

				return;
			}

			translateExpr(op1,firstFreeReg+1);
			translateExpr(op2,firstFreeReg+2);


			getCorrectMath(ast);
			codeout.println("\t" + transReg(firstFreeReg) + "," + transReg(firstFreeReg+1) + "," + transReg(firstFreeReg+2));
		}
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



	public static void translateWhile(MAST ast,int firstFreeReg)
	{
		MAST condRoot = (MAST)ast.getFirstChild();
		MAST body = (MAST)condRoot.getNextSibling();

		String condLabel = "L" + newLabel();
		String bodyLabel = "L" + newLabel();
		String endLabel = "L" + newLabel();

		codeout.println(condLabel+":");
		translateCond(condRoot,firstFreeReg,bodyLabel,endLabel,notInverted);

		translateStatList(ast,firstFreeReg);

		codeout.println("\tb\t"+condLabel);
		codeout.println(endLabel+":");
	}

	public static void translateRepeat(MAST ast,int firstFreeReg)
	{
		MAST body = (MAST)ast.getFirstChild();
		MAST condRoot = (MAST)body.getNextSibling();

		String bodyLabel = "L" + newLabel();
		String endLabel = "L" + newLabel();

		codeout.println(bodyLabel+":");
  		translateStatList(body,firstFreeReg);
		translateCond(condRoot,firstFreeReg,bodyLabel,endLabel,notInverted);
		codeout.println(endLabel+":");
	}

	public static void translateIf(MAST ast,int firstFreeReg)
	{

		MAST condRoot = (MAST)ast.getFirstChild();
		MAST successStats = (MAST)condRoot.getNextSibling();
		MAST failStats = (MAST)successStats.getNextSibling();

		String successLabel = "L" + newLabel();
		String failLabel = "L" + newLabel();

		translateCond(condRoot,firstFreeReg,successLabel,failLabel,notInverted);

		codeout.println(successLabel+":");

		translateStatList(successStats,firstFreeReg);
		if(failStats != null)
		{
			String endLabel = "L" + newLabel();
			codeout.println("\tb\t"+endLabel);
			codeout.println(failLabel+":");
			translateStatList(failStats,firstFreeReg);
			codeout.println(endLabel + ":");
		}
		else
		{
			codeout.println(failLabel+":");
		}
	}

	private static void translateCond(MAST cond,int firstFreeReg,String successLabel,String failLabel,boolean inv)
	{
		switch(cond.getType())
		{
			case(Decm2LexerTokenTypes.BOOLOP):
			{
				MAST lhs = (MAST) cond.getFirstChild();
				translateExpr(lhs,firstFreeReg);

				MAST rhs = (MAST) lhs.getNextSibling();
				translateExpr(rhs,firstFreeReg+1);

				String op = cond.getText();
				getCorrectBranch(op,inv);
				codeout.println("\t" + transReg(firstFreeReg) + "," + transReg(firstFreeReg+1) + "," +successLabel);

				codeout.println("\tb\t"+failLabel);

				break;
			}
			case(Decm2LexerTokenTypes.NOT):
			{
				MAST invertedCond = (MAST)cond.getFirstChild();
				translateCond(invertedCond,firstFreeReg,failLabel,successLabel,!inv);
				break;
			}
			case(Decm2LexerTokenTypes.AND):
			{//the RIGHT term is always atomic

				MAST firstCond = (MAST) cond.getFirstChild();
				MAST secondCond = (MAST) firstCond.getNextSibling();

				String nextCond = "L" + newLabel();
				String endAndTest = "L" + newLabel();

				translateCond(firstCond,firstFreeReg,nextCond,endAndTest,inv);

				codeout.println(nextCond+":");

				translateCond(secondCond,firstFreeReg,successLabel,failLabel,inv);
     				codeout.println(endAndTest+":");
				codeout.println("\tb\t"+failLabel);

				break;
			}
			case(Decm2LexerTokenTypes.OR):
			{
				MAST firstCond = (MAST) cond.getFirstChild();
				MAST secondCond = (MAST) firstCond.getNextSibling();

				String nextCond = "L" + newLabel();

				translateCond(firstCond,firstFreeReg,successLabel,nextCond,inv);
				codeout.println(nextCond+":");
				translateCond(secondCond,firstFreeReg,successLabel,failLabel,inv);

				break;
			}
		}
	}

	private static void getCorrectBranch(String op,boolean inv)
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
			codeout.print("bgt");
		}
	}

	private static int newLabel()
	{
		nextLabel++;
		return nextLabel;
	}

	public static String transReg(int virtReg)
	{//input will be a number

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
				default:	{gen.internalError("Out of free Registers",ast);break;}
			}
		}
		return reg;
	}

}
