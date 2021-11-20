#include <stdio.h>
#include "machine.h"

void mult1(matrixfile a, matrixfile b, matrixfile c, int rows)
{

	int resRow;
	int resCol;
	int size = rows*rows;

	diskfileread(a,size,0,0);
	diskfileread(b,size,0,size);



	for (resCol=0; resCol<rows;resCol++)
	{
		for (resRow=0; resRow<rows;resRow++)
		{
			int i;
			for i=0; i<rows;i++)
			{
				mainmemory[2*size+resRow*rows+resCol] +=
					mainmemory[resRow*rows+i]*mainmemory[size+resCol+rows*i];
			}
		}
	}

	diskfilewrite(c,size,0,2*size);
}


void mult2(matrixfile a, matrixfile b, matrixfile c, int rows)
{
	int size=rows*rows;
	int currRowA;
	diskfileread(b,size,0, rows); /* read ALL elements from matrix b,
					starting at 0, copying to mainmemory address 'rows' */

	for (currRowA=0;currRowA<rows;currRowA++) // i.e. read in one row of A at a time
	{
		int currColA;
		diskfileread(a,rows,currRowA*rows,0);

		for (currColA=0;currColA<rows;currColA++)
		{/* i.e. place the result in the appropriate temporary storage */
			double acc=0;
			int i;
			for (i=0;i<rows;i++)
			{
				acc += mainmemory[i]*mainmemory[rows+rows*i+currColA];
			}
			mainmemory[rows+rows*rows+currColA] = acc;
		}
		diskfilewrite(c,rows,rows*currRowA,rows+size);
	}
}

void mult3(matrixfile a, matrixfile b, matrixfile c, int rows)
{
 	int currRowA;
	for (currRowA=0;currRowA<rows;currRowA++)
	{
		int currRowB;
		int i;
		zeromemory();

		diskfileread(a,rows,rows*currRowA,0);//copy the next row of A to memory

		for (currRowB=0; currRowB<rows;currRowB++)
		{
			int j;
			diskfileread(b,rows,rows*currRowB,rows);
			for (j=0;j<rows;j++)
			{
				mainmemory[rows*2+j] += mainmemory[currRowB]*mainmemory[rows+j];
			}
		}
		diskfilewrite(c,rows,rows*currRowA,rows*2);//write the row in c to disk
	}
}
