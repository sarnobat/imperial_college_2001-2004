#ifndef MACHINE_H
#define MACHINE_H 1
/* The interface to the `machine' module */
#define MAXMATRIXSIZE 266
#define MEMORYSIZE 800
#define MAXFILESIZE  100000
#define FALSE 0
#define TRUE (!FALSE) 

typedef double memory[MEMORYSIZE];
typedef int fileposition;
typedef int address;

/* matrixfile is an abstract type implemented in machine.c 
    To reduce the amount of disk space used and improve the time taken
    to run the program the type matrixfile  is stored in memory. */
typedef /*abstract*/   void * matrixfile ;

extern memory mainmemory; /* memory is an array*/

matrixfile createfile(int writeable); /* returns an empty matrixfile */

void deletefile(matrixfile f) ; /* remove a matrixfile */ 

void initialisematrices(int size, matrixfile a, matrixfile b);
/* post:  matrixfiles a and b are filled with  size*size random numbers  */

void diskfileread(matrixfile input, int count, fileposition fileaddress,
                  address mainaddress);
/*   Reads count elements from the matrix file input
 pre: initialisematrices has been called, count > 0 and
      fileaddress + count < MAXFILESIZE and mainaddress + count < MEMORYSIZE
      (the program stops if the pre-condition does not hold )
 post: input[fileaddress .. fileaddress+count-1] is copied to
       mainmemory[mainaddress .. mainaddress+count-1]
       The count of reads performed is incremented
*/

void diskfilewrite(matrixfile output, int count, fileposition fileaddress,
		   address mainaddress);
/* pre: initialisematrices has been called, count > 0 and
      fileaddress + count < MAXFILESIZE and  mainaddress + count < MEMORYSIZE
      (the program  stops if the pre-condition does not hold )
   post: mainmemory[mainaddress .. mainaddress+count-1] is written to
       output[fileaddress .. fileaddress+count-1]
       The count of writes performed is incremented  */

void printfile(matrixfile from, int count,
	       fileposition fileaddress);
/* pre: initialisematrices has been called, count > 0 and
      fileaddress + count < MAXFILESIZE
post:    from[fileaddress .. fileaddress+count-1] is printed on stdout
*/

void zeromemory();
/* post: mainmemory is set to zero */
#endif
