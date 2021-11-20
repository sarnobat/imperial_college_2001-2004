		/*if(isAtomic(op2))
		{
			if (op2.getType() == Decm2LexerTokenTypes.ID)
			{
				op2Str = "_" + op2Str;
				codeout.println("\tlw\t$t" + (firstFreeReg+2) +"," + op2Str);
			}
			else
			{
				codeout.println("\tli\t$t" + (firstFreeReg+2) +"," + op2Str);
			}

			getCorrectMath(ast);
			codeout.println("\t$t" + firstFreeReg + ",$t" + (firstFreeReg+1) + ",$t" + (firstFreeReg+2));
		}
		else
		{
			translateExpr(op2,firstFreeReg+1);
			getCorrectMath(ast);
			codeout.println("\t$t" + destReg + ",$t" + (destReg+1) + ",$t" + (destReg+2));
		}*/

		/*if (isAtomic(op1))
		{
			if (ast.getType() == Decm2LexerTokenTypes.ID)
			{//the following if statements add a '_' if the operand is a
			//	vairable name
				op1Str = "_" + op1Str;
				codeout.println("\tlw");
			}
			else
			{
				codeout.println("\tli");
			}
			codeout.print("\t$t" + (firstFreeReg+1) +"," + op1Str);
		}
		else
		{
			translateExpr(ast,firstFreeReg+1);
			firstFreeReg++;
		}*/
/make sure labels are consistent; it's a risky policy trying to reuse code for while
	//function too
		String end = "L" + newLabel();
		MAST condRoot = (MAST)ast.getFirstChild();
		MAST secondStatList = (MAST) condRoot.getNextSibling().getNextSibling();

		if (secondStatList == null)
		{//there is no 'else' part

			translateCond(condRoot,firstFreeReg,false,notInverted);
			codeout.println("\tb\t" + end);
			//translate statements
			codeout.println(end+":");
		}
		else
		{//there is an 'else' part

			String elseLabel = "L" + newLabel();
			translateCond(condRoot,firstFreeReg,true,notInverted);

			//translate statements
			codeout.println("\tb\t" + end);
			codeout.println(elseLabel+":");
			//translate statements
			codeout.println(end+":");
		}
		/**/
	}

	public static void translateWhile(MAST ast,int firstFreeReg)
	{

		/*String test = "L" + newLabel();
		String end = "L" + newLabel();
		codeout.println(test + ":");
		translateCond(ast.getFirstChild(),firstFreeReg)
		codeout.println("\tb\t" + test);
		codeout.println(end);*/
	}


	public static void translateCond(MAST ast,int firstFreeReg,boolean hasElse,boolean inverted)
	{//ast root node will be 'BOOLOP' or 'AND/OR'
  		System.out.println(ast.getText());

		if(isAtomicBool(ast))
		{//condition is atomic (i.e. BOOLOP)

			MAST lhs = (MAST) ast.getFirstChild();
			translateExpr(lhs,firstFreeReg);

			MAST rhs = (MAST) lhs.getNextSibling();
			translateExpr(rhs,firstFreeReg+1);

			getCorrectBranch(ast.getText(),inverted);

			String ifLabel = "L" + newLabel();
			codeout.println("\t$t" + firstFreeReg + ",$t" + (firstFreeReg+1) + "," +ifLabel);
			codeout.println("\tb\t" + elseLabel);
			codeout.println(ifLabel+":");
		}
		else
		{//condition contains an AND or OR or NOT
			translateCompCond(ast,firstFreeReg,hasElse,inverted);
		}
	}

	public static void translateCompCond(MAST ast,int firstFreeReg,boolean hasElse,boolean inverted)
	{
		switch(ast.getType())
		{
			case (Decm2LexerTokenTypes.NOT):
			{

				MAST invertedCond = (MAST)ast.getFirstChild();
				//root of invertedCond will be the one below the NOT node
				translateCond(invertedCond,firstFreeReg,hasElse,!inverted);
				break;
			}
			case (Decm2LexerTokenTypes.AND):
			{
				MAST firstCond = (MAST)ast.getFirstChild();
				MAST restConds = (MAST)firstCond.getNextSibling();
				translateCond(firstCond,firstFreeReg,hasElse,inverted);
				String secondTest = "L" + newLabel();
				codeout.println(secondTest);
				translateCond(restConds,firstFreeReg,hasElse,inverted);
				break;
			}
			case (Decm2LexerTokenTypes.OR):
			{
				break;
			}
		}
	}

	private static boolean isAtomicBool(MAST ast)
	{
		return (ast.getType() == Decm2LexerTokenTypes.BOOLOP);
	}



	private static void getCorrectBranch(String op,boolean inverted)
	{
		codeout.print("\t");
		if (!inverted)
		{
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
		else
		{//we require branch on OPPOSITE condition
			if(op.equals("<"))
			{
				codeout.print("bge");
			}
			else if(op.equals("<="))
			{
				codeout.print("bg");
			}
			else if(op.equals("="))
			{
				codeout.print("bne");
			}
			else if(op.equals("#"))
			{
				codeout.print("be");
			}
			else if(op.equals(">="))
			{
				codeout.print("blt");
			}
			else if(op.equals(">"))
			{
				codeout.print("ble");
			}
		}
	}



















	public static void translateCond(MAST cond,int firstFreeReg,String successLabel,String failLabel,boolean inv)
	{//THIS ISN'T RIGHT FOR SOME MORE COMPLEX ONES
//System.out.println(successLabel);
		switch(cond.getType())
		{
			case(Decm2LexerTokenTypes.BOOLOP):
			{//only print where to branch if successful test
				//translate atomic condition

				MAST lhs = (MAST) cond.getFirstChild();
				translateExpr(lhs,firstFreeReg);

				MAST rhs = (MAST) lhs.getNextSibling();
				translateExpr(rhs,firstFreeReg+1);

				String op = cond.getText();
				getCorrectBranch(op,inv);

System.out.println(successLabel);

				codeout.println("\t$t" + firstFreeReg + ",$t" + (firstFreeReg+1) + "," +successLabel);

				//codeout.println("\tb\t"+failLabel);

				break;
			}
			case(Decm2LexerTokenTypes.NOT):
			{
				//translate inversion
				MAST invertedCond = (MAST)cond.getFirstChild();
				translateCond(invertedCond,firstFreeReg,failLabel,successLabel,!inv);

				codeout.println("\tb\t"+successLabel);
				codeout.println(successLabel+":");

				break;
			}
			case(Decm2LexerTokenTypes.AND):
			{
				//translate conjunction
				/*MAST firstCond = (MAST) cond.getFirstChild();
				MAST secondCond = (MAST) firstCond.getNextSibling();
				String nextCond = "L" + newLabel();
				String endAndTest = "L" + newLabel();

				translateCond(firstCond,firstFreeReg,nextCond,failLabel,inv);

				codeout.println("\tb\t"+endAndTest);
				codeout.println(nextCond+":");

				if (secondCond.getType() == Decm2LexerTokenTypes.BOOLOP)
				{
					translateCond(secondCond,firstFreeReg,successLabel,failLabel,inv);
				}
				else
				{
					String nextCond2 = "L" + newLabel();
					translateCond(secondCond,firstFreeReg,nextCond2,failLabel,inv);
				}

				codeout.println(endAndTest+":");

				//codeout.println("\tb\t"+failLabel);*/
				break;
			}
			case(Decm2LexerTokenTypes.OR):
			{
				//translate disjuntion
				MAST firstCond = (MAST) cond.getFirstChild();

				MAST secondCond = (MAST) firstCond.getNextSibling();

				/*if (inv == false)
				{*/
					translateCond(firstCond,firstFreeReg,successLabel,failLabel,inv);
					translateCond(secondCond,firstFreeReg,successLabel,failLabel,inv);
				/*}
				else
				{
	System.out.println("HERE");
					translateCond(firstCond,firstFreeReg,failLabel,successLabel,inv);
					translateCond(secondCond,firstFreeReg,failLabel,successLabel,inv);
				}*/

				//codeout.println("\tb\t"+failLabel);


				codeout.println("\tb\t"+failLabel);
				codeout.println(successLabel+":");

				break;
			}
		}
		//codeout.println("\tb\t"+failLabel);

	}
