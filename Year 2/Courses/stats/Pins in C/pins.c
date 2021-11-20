#include <stdio.h>
#include <stdlib.h>

#define U 1
#define D 0
#define NUMBEROFPINS 20

int main(void)
{
	void pinsExpt(void);
	void analyseRandomNumber(void);
	analyseRandomNumber();
	//pinsExpt();
	return 1;
}

void pinsExpt(void)
{
	int i;
	int noOfPinsUp = 0;
	double cumPropnPinsUp=0;

	int pins[NUMBEROFPINS] = { 	D,D,U,U,U,
					D,D,D,D,D,
					U,D,D,U,D,
					D,U,D,U,D	};

	for (i=0;i<NUMBEROFPINS;i++)
	{
		if (pins[i]==1)
		{
			noOfPinsUp++;
			//cumPropnPinsUp++;
		}
		cumPropnPinsUp = noOfPinsUp/((double) (i+1));
		printf("%f\n",cumPropnPinsUp);
	}

	print
	/* printf("Total number of pins with sharp end pointing up: %i\n",noOfPinsUp );*/
}

void analyseRandomNumber(void)
{
	int i;
	int noPositive = 0;
	double cumPropnPinsUp=0;
	for (i=0;i<100;i++)
	{
		double a;
		double b;
		double diff;
		a = rand();
		b = rand();
		diff = a-b;
		if (diff>0)
		{
			noPositive++;
		}
		cumPropnPinsUp = noPositive/((double) (i+1));
		printf("%f\n",cumPropnPinsUp);
		/* printf("%f\n",diff);
		printf("Number of positive differences out of 100 = %i",noPositive);*/
	}
}
