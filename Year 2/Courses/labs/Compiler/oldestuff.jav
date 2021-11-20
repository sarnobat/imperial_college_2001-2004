
	public static void translateCond(MAST cond,int firstFreeReg,String successLabel,String failLabel,boolean inv)
	{
		translateBoolExpr(cond,firstFreeReg,successLabel,failLabel,inv);
		switch(cond.getType())
		{
			case(Decm2LexerTokenTypes.BOOLOP):
			{
				break;
			}
			case(Decm2LexerTokenTypes.NOT):
			{

				break;
			}
			case(Decm2LexerTokenTypes.AND):
			{
				break;
			}
			case(Decm2LexerTokenTypes.OR):
			{
				break;
			}
		}

	}

	private static void translateBoolExpr(MAST cond,int firstFreeReg,String successLabel,String failLabel,boolean inv)
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
				codeout.println("\t$t" + firstFreeReg + ",$t" + (firstFreeReg+1) + "," +successLabel);
				break;
			}
			case(Decm2LexerTokenTypes.NOT):
			{
				MAST invertedCond = (MAST)cond.getFirstChild();
				translateBoolExpr(invertedCond,firstFreeReg,failLabel,successLabel,!inv);
				break;
			}
			case(Decm2LexerTokenTypes.AND):
			{

				MAST firstCond = (MAST) cond.getFirstChild();
				MAST secondCond = (MAST) firstCond.getNextSibling();
				String nextCond = "L" + newLabel();
				String endAndTest = "L" + newLabel();

				translateBoolExpr(firstCond,firstFreeReg,nextCond,failLabel,inv);

				codeout.println("\tb\t"+endAndTest);
				codeout.println(nextCond+":");

				if (secondCond.getType() == Decm2LexerTokenTypes.BOOLOP)
				{
					translateBoolExpr(secondCond,firstFreeReg,successLabel,failLabel,inv);
					codeout.println(endAndTest+":");
				}
				else
				{
					String nextCond2 = "L" + newLabel();
					translateBoolExpr(secondCond,firstFreeReg,nextCond2,failLabel,inv);
				}


				break;
			}
			case(Decm2LexerTokenTypes.OR):
			{
				MAST firstCond = (MAST) cond.getFirstChild();
				MAST secondCond = (MAST) firstCond.getNextSibling();

				translateBoolExpr(firstCond,firstFreeReg,successLabel,failLabel,inv);
				translateBoolExpr(secondCond,firstFreeReg,successLabel,failLabel,inv);
				codeout.println("\tb\t"+failLabel);
				break;
			}
		}
	}