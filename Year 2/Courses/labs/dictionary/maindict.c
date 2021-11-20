/* Test harness for  the dictionary */
/* Ian Moor 199?-2000 */

#define LINELENGTH 256

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <ctype.h>
#include "dict.h"



static void showmenu() {
  printf("Dictionary program:\n");
  printf("Commands that are recognised (and can be abbreviated) are:\n");
  printf("read    <filename>  Fill the dictionary with the contents of <filename>\n");
  printf("list                Display the dictionary contents on the screen\n"); 
  printf("search  <word>      Is <word> in the dictionary?\n"); 
  printf("add     <word>      Add <word> to the dictionary \n");
  printf("delete  <word>      Delete the <word> from the dictionary\n");
  printf("empty               Remove all words from the dictionary\n");
  printf("count               Show the number of words in the dictionary\n");
  printf("find    <number>    Show word <number> in dictionary\n");
  printf("write   <filename>  Write the contents of the dictionary to <file\n");
  printf("help                Print the list of commands for this program \n");
  printf("quit                Exit this program\n");
}

static char * commands [] = /* expected commands */
  { "quit","help","list","count","empty","read","write",
    "search","add","delete","find"};

/* representing the commands */
enum   cmds { cmd_quit,cmd_help,cmd_list,cmd_count,cmd_empty,
              cmd_read,cmd_write,cmd_search,cmd_add,cmd_delete,cmd_find,
              cmd_end
}; 


static int getargument( char value[], enum cmds it) {
/* the command requires an argument, get the next token */
  char * argument = strtok(NULL," \n");
  if (argument == NULL)  {
    fprintf(stderr,"%s must have an argument\n",commands[it]); 
    return FALSE;
  }
  strcpy(value,argument);
  return TRUE;
 }
 

static int docommand(char * line) {
  /*
    check that the line contains a command and an argument if required.
    call the appropriate function and report the result. 
    returns TRUE (non-zero) to quit
   */

  enum cmds todo;
  
  char *c ;
  char command[LINELENGTH];
  char argument[LINELENGTH];
  int count = 0;
  int i,n;
  /* the working  dictionary *
     it is static to keep the result between commands
  */
   static dict thedictionary ;

  FILE * infile, * outfile ;
  word aword ; 

/* skip leading blanks in the line */
  while (*line == ' ')
    line ++ ;

/* get the first word on the line */
  c = strtok(line," \n"); 
  if ( c == NULL ) /* no words on the line */
    return FALSE;
/* copy the command and convert it to lowercase */
  strcpy(command,c);
  for (i=0;command[i] != '\0';i++)
    command[i]=tolower((unsigned char)command[i]);
  
/* search the list of commands for the word
   look for the first item in  commands starting with command
*/
  for (todo = cmd_quit; todo < cmd_end; todo++ ) {
    if (strstr(commands[todo],command) == commands[todo]) {
        /* the input matches a command */
      break;
    }
  }

  /* select the operation on the tree */
  switch (todo) {

  case cmd_read:
    if (!getargument(argument,todo)) 
      break;
    if (NULL != (infile = fopen(argument,"r"))) {
      thedictionary = readdict(infile,thedictionary);
      (void)fclose(infile);
    }
    else {
      fprintf(stderr,"could not open file %s for reading: %s\n ",argument,
              strerror(errno));
    }
    
    break;
    
  case cmd_write:
    if (!getargument(argument,todo)) 
      break;
    if (NULL != (outfile = fopen(argument,"w"))) {
      writedict(outfile,thedictionary);
      (void)fclose(outfile);
    }
    else {
      fprintf(stderr,"could not open file %s for writing: %s\n",argument,
              strerror(errno));
    }
    
    break;
    
  case cmd_list:
    writedict(stdout,thedictionary);
    break;
    
  case cmd_search:
    if (!getargument(argument,todo)) 
      break;
    if (lookup(argument,thedictionary) )
      printf("The word '%s' is in the dictionary\n",argument);
    else
      printf("The word '%s' is not in the dictionary\n",argument);
    break;
    
  case cmd_add:
    if (!getargument(argument,todo)) 
      break;
    thedictionary = addword(argument,thedictionary);
    break;
    
  case cmd_delete:
    if (!getargument(argument,todo)) 
      break;
    thedictionary = deleteword(argument,thedictionary);
    break;
    
  case cmd_empty:
    deletedict(thedictionary);
    thedictionary = createdict();
    break;
    
  case cmd_count:
    n = countwords(thedictionary);
    if (n == 1)
      printf("There is one word in the dictionary\n");
   else    
     printf("There are %d words in the dictionary\n",n);
    break;
    
  case cmd_find:
     if (!getargument(argument,todo)) 
      break;
   /* convert the argument to a number */
      if (sscanf(argument," %d",&count) == 1) {
	if ( findword(thedictionary,count,aword))
	  printf("Word number %d  in the dictionary is '%s'\n",count,aword);
	else
	  printf("Word number %d  in the dictionary is undefined \n",count);
      }
      else
	fprintf(stderr,"find must have a number as an argument\n");
      break;
      
  case cmd_quit:
	return  TRUE;

  case cmd_help:
    showmenu();
    break;

  default:
    fprintf(stderr,"Unrecognised command %s\n",command);
  }
  return FALSE;
}

/* main program */
int main (void){
  char line [ LINELENGTH ] ;
  
  showmenu();
  do {
    (void)fputs("Command? ",stdout);
    (void)fflush(stdout);
      /* ends when no more input or a quit cimmand */
  } while ((fgets(line,LINELENGTH,stdin) != NULL) && 
	 !docommand(line));
  return EXIT_SUCCESS;
}


