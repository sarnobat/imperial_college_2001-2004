/*The test harness for limited memory exercise
   It gives  a choice of  algorithm and matrix size, initializes
   the matrices and checks the result */
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include "machine.h"
#define MAXLINE 256

/* prototypes for multiplication functions defined in mult.c */
extern void mult1(matrixfile a, matrixfile b, matrixfile c, int rows );
extern void mult2(matrixfile a, matrixfile b, matrixfile c, int rows );
extern void mult3(matrixfile a, matrixfile b, matrixfile c, int rows );

/* protypes for functions defined in machine.c */
extern void resetstatistics();
/* post: the count of reads and writes is zero */


extern void statistics (int * reads, int * writes );
/* pre: resetstatistics has been called at least once
   post: the number of read and write operations
	 since the last reset are returned in reads and writes  */


extern int wronganswer(matrixfile a,matrixfile b, matrixfile c,int why);
/* pre: initialisematrices has been called at least once
   post: returns TRUE  if c does not contain the product
	 of the two matrixfiles a and b
         if 'why' is true and the c is incorrect the value of the first
         incorrect element of c is printed
   */
extern void initialisematrices(int size, matrixfile a, matrixfile b) ;
    /* put values in a,b,c a b != c at the end*/

static void chomp( char * inputline ) {
    /* remove any newline characters from a string */
  int ends = (int)strcspn(inputline,"\r\n");
  if ( ends > 0 )
    inputline[ends]='\0';
}

static void check(int algorithm,matrixfile a,matrixfile b,
                  matrixfile c,
		  int size){
  /* check the results of one multiplication */
  int reads,writes ;
  printf("Statistics for MatrixMult %d\n",algorithm);
  statistics(&reads,&writes);
  printf("Your multiplication of matrices, size %d\n performed %d reads and %d writes\n",size,reads,writes);
  if (reads == 0 || writes == 0 || wronganswer(a,b,c,0))
    printf("the result is *INCORRECT*\n");
  else
    printf("the result is correct\n");
}

static void setsize (  int *size, matrixfile a, matrixfile b){
  /* read the matrix  size and initialise the input maxtrix files */
  int thesize = 0;
  int wasread ;
  char result[MAXLINE];
  for(;;) {
    printf("How big should the matrices be (1..%d)? ",MAXMATRIXSIZE);
    wasread = scanf(" %d",&thesize);
    if ( wasread == EOF )
      exit (EXIT_SUCCESS);
    if ( wasread == 1 ) { /* a number */
      if ((thesize>0) && (thesize<=MAXMATRIXSIZE)) {
	break; /* correct size */
      }
      else {
	printf("%d was not between 1..%d try again\n",thesize,MAXMATRIXSIZE);
      }
    }
    else {
        /* not a number: read what was typed, quit at end of input */  
      if (fgets(result,MAXLINE,stdin) == NULL)
        exit (EXIT_SUCCESS);
      chomp(result);
      printf("'%s' is not a number, try again\n",result);
    }
  }

  /*a valid number*/
  *size = thesize;
  printf("Generating matrices of size %d ...",*size);
  initialisematrices(*size,a,b);
  printf(" done.\n");
}



/* main program*/

int main(){
  matrixfile
    a = createfile(0),
    b = createfile(0);
  int size,option;
  char reply[MAXLINE];

    /* initialise the random number sequence */
  srand((unsigned int)getpid());
  printf("\nTwo Level Store matrix Multiplication\n");

  setsize(&size,a,b);

  for(;;) {
/* main test loop */
    int wasread;
    matrixfile  c = createfile(1);
    resetstatistics();
    zeromemory();
    printf("1,2 or 3: Multiply matrices using the algorithm\n");
    printf("n: Generate new matrices\n");
    printf("q: Quit\n");
    printf("Input a value 1..3, n or q: ");

    wasread = scanf(" %d",&option);
    if (wasread == EOF){
      exit(EXIT_SUCCESS);   /* quit at end of input */
    }
    
    if ( wasread != 1){
	/* not a number, read what was typed */
      if ((fgets(reply,MAXLINE,stdin) == NULL) || reply[0]=='q' ||
	  reply[0]=='Q')
	exit(EXIT_SUCCESS);
      if (reply[0] == 'n' || reply[0] == 'N'){
	setsize(&size,a,b);
	continue;
      }
      chomp(reply);
      printf("'%s': was not  recognized input\n",reply);
      continue;
    }

    if (( option == 1 && size < 17) || ( option == 2 && size < 28 ) ||
        ( option == 3 && size < 267 )) {
       
      printf("Mult%d starts now...",option);
      (void)fflush(stdout);
      switch(option){
          case 1:
            mult1(a,b,c,size);
            break;
          case 2:
            mult2(a,b,c,size);
            break;
          case 3:
            mult3(a,b,c,size);
            break;
      }
      printf("..done\n");
      check(option,a,b,c,size);
      deletefile(c);
    }
    else if ( option < 1 || option > 3 ) {
      printf("There is no multiply algorithm %d\n",option);
      continue;
    }
    else {
      printf("Algorithm %d cannot multiply arrays of size %d\n",option,size);
      continue;
    }
    
  }
  deletefile(a);
  deletefile(b);
  exit(EXIT_SUCCESS);
}
