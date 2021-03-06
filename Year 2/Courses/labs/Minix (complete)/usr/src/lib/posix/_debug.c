#include <lib.h>
#include </usr/include/minix/config.h>
#include </usr/include/minix/com.h>

#define debug _debug

#if ENABLE_DEBUGGING

PUBLIC int debug(char * pointer, int integer, long address){
  message mess; 
  /* Cr tes a message which will contain the above parameters */

  mess.ADDRESS = pointer; 
  /* The place where the Program Discriptor (PD) of the child is copied to */
  mess.REQUEST = integer; 
  /* The request that is passed to the task */
  mess.POSITION = address; 
  /* The place where to put the break-point in the child's code, ONLY if integer is DEBUG_BREAKPOINT */

  return(_syscall(FS, DEBUG, &mess)); 
  /* Sends a the address of mess to FS of type DEBUG */
}

#endif
