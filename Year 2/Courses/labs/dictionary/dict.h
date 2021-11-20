/*
 header file defining types and function prototypes  for the dictionary.
*/
#define FALSE 0
#define TRUE (! FALSE)
#define MAXWORDSIZE 100

typedef char word[MAXWORDSIZE];
typedef struct dictionary *dict;

/* the structure used to construct the dictionary */
 struct dictionary {
   dict left,right;
   word theword;
 } ;

/* prototypes for public functions defined in dict */

dict createdict(void) ;
/* post: the value returned is a valid dictionary containing no words */ 

dict addword(char w[], dict d );
/* 
pre:  d is a valid dictionary
post: the result contains all the words of d and the word w added
      in the correct (alphabetic) place
*/

 dict readdict(FILE *in, dict d );
/*
pre:  d is a valid dictionary and 'in' has been opened for reading
      and contains zero or more words, one word per line
post: the resulting dictionary contains all the words in d or in 'in'
*/ 

void writedict(FILE *out, dict d);
/* 
pre:  d is a valid dictionary and 'out' has been opened for writing
post: the words in d are written to 'out' one per line in
      alphabetic order
*/

int lookup( char w[],  dict d );
/*
pre:  d is a valid dictionary
post: the value returned is TRUE (non-zero) if w appears in d
*/

int countwords( dict d);
/*
pre:  d is a valid dictionary
post: the result is the number of words in d 
*/

int findword( dict d, int n, char w[] );
/*
pre:  d is a valid dictionary
post: if  0 < n  <= countwords(d) the nth word in alphabetic 
      order is copied into w and TRUE(non-zero) is returned.
      Otherwise w is not changed and FALSE is returned
*/

dict deleteword(char w[],  dict d );
/*
pre:  d is a valid dictionary
post: the result contains all the words in d  except w and the
      storage used by w is reclaimed
*/

void deletedict(dict d );
/*
pre:  d is a valid dictionary
post: all storage used by d is reclaimed
 */

