/* The implementation of the matrixfile type */
#include <stdio.h>
#include <stdlib.h>
/* #include <limits.h> */
#ifdef __BOUNDS_CHECKING_ON
/* the bound checking gcc does not like the inline math coprocessor code */
#define  __NO_MATH_INLINES 1
#endif
#include <math.h>
#include <assert.h>
#include <float.h>
/* machine.h is not include. it has a different (abstract) definition of
   matrixfile. This is the implementation of the abstract type in machine.h */
#define MAXMATRIXSIZE 266
#define MEMORYSIZE 800
#define MAXFILESIZE  100000
#define FALSE 0
#define TRUE (! FALSE)

typedef double memory[MEMORYSIZE];
typedef   double * filerow    ;
typedef struct fileobj {
  filerow rows [MAXMATRIXSIZE];
  int writeable ;
} fileobject ;
typedef  fileobject * matrixfile ;

typedef int fileposition;
typedef int address;

static int noofreads,noofwrites;
static int currentsize;

memory mainmemory;

void resetstatistics(){
  noofreads = 0;
  noofwrites = 0;
}

matrixfile createfile(int writeable) {
    /* returns  an empty matrixfile */ 
  int row;
    /*  places for the rows of the file */
  matrixfile r = (matrixfile) malloc(sizeof(fileobject));
  assert(r != NULL);
  
  for(row=0;row<MAXMATRIXSIZE;row++){
    r -> rows[row] = NULL;
  }
  r -> writeable = writeable;
  return r;
}

static void resetfile(matrixfile f) {
    /* free all the rows in a matrixfile */
  int row ;
  for (row = 0; row < currentsize; row++ ) {
    if ( f -> rows[row] != NULL) {
      free(f->rows[row]);
      f -> rows[row] = NULL;
    }
  }
}

void deletefile(matrixfile f){
/* remove a matrixfile */
  resetfile(f);
  free(f);
}

static void generatematrixfile( matrixfile m,int zero){
  int row,col;
    /* generate a new matrix file
       remove any previous rows (which might be shorter than required)
       fill the matrix with 0 or random numbers */
  resetfile(m);
  for (row=0;row<currentsize;row++){
    filerow rowi = malloc(sizeof(double)*currentsize);
    assert(rowi!=NULL);
    m -> rows[row] = rowi;
    if (zero)
      for (col=0;col<currentsize;col++){
	rowi[col]= 0.0;
      }
    else
      for (col=0;col<currentsize;col++){
	rowi[col]=(double) (rand()) / (double) RAND_MAX ;
      }
  }
}

void statistics(int *r, int *w){
  *r = noofreads;
  *w = noofwrites;
}


static void multiply(matrixfile a, matrixfile b, matrixfile c){
/* calculate muliply in the file */
  int row,col,index;
  double sum;
  filerow arow, brow, crow ;

  for (row=0;row<currentsize;row++){
    arow = a->rows[row];
    assert(arow != NULL);
    crow = c->rows[row];
    assert( crow != NULL );
    for (col=0;col<currentsize;col++){
      sum=0.0;
      for (index=0;index<currentsize;index++){
	brow=  b->rows[index];
        assert(brow != NULL);
	sum+= arow[index]*brow[col];
      }
      crow[col]=sum;
    }
  }
}

int wronganswer(matrixfile a, matrixfile b, matrixfile c,int why) {
  /* returns |a * b - c| < epsilon ( a small number)
     if why is true print out the actual and expected values at the first item
     which is different.
     NB FLT_EPSILON is used not DBL_EPSILON to avoid rounding  errors
  */
  int row,col;
  matrixfile result = createfile(1);
  generatematrixfile(result,1);
  multiply(a,b,result);
  for(row=0;row<currentsize;row++){
    filerow crow = c->rows[row],
      resultrow = result->rows[row];
    assert(crow!=NULL);
    assert(resultrow!= NULL);
    for(col=0;col< currentsize;col++){
      if (fabs(crow[col]-resultrow[col]) > FLT_EPSILON){
        if (why)
          printf("incorrect at row %d col %d c: %18.16f expected %18.16f diff %18.16f \n",row,col,
                crow[col],resultrow[col],
                 fabs(crow[col]-resultrow[col]));
        deletefile(result);
	return TRUE;
      }
    }
  }
  deletefile(result);
  return FALSE;
}

static int allzero( matrixfile m) {
/* true if the whole matrixfile m is zero */
  int row;
  for (row=0;row<currentsize;row++){
    int col;
    filerow  mrow = m->rows[row];
    if (mrow == NULL)
      return FALSE;
    for (col=0;col<currentsize;col++){
      if (fabs(mrow[col]) >  FLT_EPSILON)
	return FALSE;
    }
  }
  return TRUE;
}



void initialisematrices(int size, matrixfile a, matrixfile b) {
    /* put random values in a,b such that  a b != 0*/
 matrixfile result ;
 int zeroproduct;
  currentsize = size;
  assert (size > 0 && size <= MAXMATRIXSIZE);
  result =  createfile(1);
  do {
    generatematrixfile(a,0);
    generatematrixfile(b,0);
    generatematrixfile(result,1);
    multiply(a,b,result);
    zeroproduct = allzero(result); /* if a b = 0 try again */
    deletefile(result);
  } while (zeroproduct);
}

void diskfileread (matrixfile from, int count,
		   fileposition fileaddress, address mainaddress){
    /*read (part of) a matrix from disk
      Pre: initialisematrices has been called, count>0 and
      fileaddress+count<maxfilesize and
      mainaddress+count<topofmemory
      Post: from[fileaddress...fileaddress+count-1] is copied to
      memory[mainaddress...mainaddress+count-1]
      the counts of reads is incremented
    */

  int i,col,row;

  assert(count>0);
  assert(from != NULL);
  assert(fileaddress+count-1 <=currentsize*currentsize);
  assert(mainaddress+count-1<=MEMORYSIZE);

  col=fileaddress % currentsize;
  row=fileaddress/currentsize;

  for(i=0;i<count;i++){
    double * block = from->rows[row];
    if ( block == NULL ) {
      fprintf(stderr,"diskfileread: block %d does not contain data\n",row);
      abort();
    }

    mainmemory[mainaddress+i]= block[col++];
    if (col>= currentsize){
      col=0;
      row++;
    }
  }
  noofreads++;
}



void diskfilewrite   (matrixfile to, int count, fileposition fileaddress,
		      address mainaddress){
    /*write (part of) a matrix to disk
      Pre: initialisematrices has been called, count>0 and
      fileaddress+count<maxfilesize and
      mainaddress+count<topofmemory

      Post: mainmemory[mainaddress...mainaddress+count-1] is copied to
      to[fileaddress...fileaddress+count-1]
      the counts of writes is incremented
    */

  int i,col,row;

  assert(count>0);
  assert(to !=NULL);
  assert(fileaddress+count-1  <= currentsize*currentsize);
  assert(mainaddress+count-1 <=MEMORYSIZE);
  col=fileaddress % currentsize;
  row=fileaddress/currentsize;
  if ( ! to -> writeable ) {
    fprintf ( stderr, "Trying to write a 'read only' matrixfile\n");
    abort();
  }
  
  for(i=0;i<count;i++){
    double * block = to->rows[row];
     if ( block == NULL ) {
       block = (filerow) malloc(sizeof(double)*currentsize);
       assert(block != NULL);
       to->rows[row] = block;
     }

    block[col++]=mainmemory[mainaddress+i];
    if (col>=currentsize){
      col=0;
      row++;
    }
  }
  noofwrites++;
}

void printfile (matrixfile from, int count, fileposition fileaddress) {
/*
  pre: initialisematrices has been called, count > 0 and
      fileaddress + count < MAXFILESIZE
  post:    from[fileaddress .. fileaddress+count-1] is printed on the terminal
  for debugging 
*/
  int i,col,row;

  assert(count>0);
  assert(from != NULL);
  assert(fileaddress+count-1 <=currentsize*currentsize);

  col=fileaddress % currentsize;
  row=fileaddress/currentsize;

  for(i=0;i<count;i++){
    double * block = from->rows[row];
    if ( block == NULL ) {
      fprintf(stderr,"diskfileread: block %d does not contain data\n",row);
      abort();
    }

    fprintf(stdout,"%18.16f",block[col++]);
    if (col>= currentsize){
      col=0;
      row++;
    }
  }
}


void zeromemory(){
  address i;
  for (i=0;i<MEMORYSIZE;i++){
    mainmemory[i]=0.0;
  }
}
