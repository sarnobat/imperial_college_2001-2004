#include <minix/com.h>
#include "mm.h"
#include <signal.h>
#include "mproc.h"
#include "glo.h"
#includ <minix/type.h>

#if ENABLE_DEBUGGING
PUBLIC int do_debugstart(){

  int child_procNo = mm_in.PROC_NR; 
  printf("child process number: %d\n", child_procNo);
  mproc[child_procNo].mp_flags |= DEBUG_FLAG;
  printf("do_debugstart: FLAG RAISED\n");
  return OK;
}
#endif
