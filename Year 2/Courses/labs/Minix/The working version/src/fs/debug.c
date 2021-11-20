#include "fs.h"
#include "file.h"
#include "fproc.h"
#include "inode.h"
#include "param.h"
#include <minix/callnr.h>
#include <minix/com.h>

#if ENABLE_DEBUGGING

PUBLIC int do_debug(){
  
  printf("This is the address: %d\n", m.ADDRESS);
  printf("This is the request: %d\n", m.REQUEST);
  printf("This is the position: %dl\n", m.POSITION);
  
  printf("Entered do_debug() in fs\n");

  m.PROC_NR = who;
  m.m_source = FS_PROC_NR;

  switch(m.REQUEST){
	case DEBUG_START:{
      		printf("fs: DEBUG_START\n");
    		sendrec(MM_PROC_NR, &m);
		/* forwarding from FS to MM */   
 		printf("fs(debug_start): MESSAGE RETURN FROM MM\n");
		break;
	}

	/* stage 4 */
	case DEBUG_PROCADM:{
		m.m_type = INSTRUCTION;
		printf("fs: DEBUG_PROCADM\n");
		sendrec(DEBUGGER, &m);
		printf("fs(debug_procadm): MESSAGE RETURNED FROM DEBUGGER\n");

		if(m.REP_STATUS == SUSPEND){
			suspend(DEBUGGER);
		}
		break;
	}

	/* stage 6 */
	case DEBUG_STEP:{
		m.m_type = INSTRUCTION;
		printf("fs: DEBUG_STEP\n");
		sendrec(DEBUGGER, &m);
		printf("fs(debug_step): MESSAGE RETURNED FROM DEBUGGER\n");
		if(m.REP_STATUS == SUSPEND)
		{
			suspend(DEBUGGER);
			printf("PARENT SUSPENDED\n");
		}
		break;
	}

	case DEBUG_BREAKPNT:{
		m.m_type = INSTRUCTION;
		printf("fs: DEBUG_BREAKPNT");
		sendrec(DEBUGGER, &m);
		if (m.REP_STATUS == SUSPEND) {
			suspend(DEBUGGER);
		}
		printf("fs(debug_breakpnt): MESSAGE RETURNED FROM DEBUGGER\n");
		break;
	}

	case DEBUG_RESUME:{
		m.m_type = INSTRUCTION;
		printf("fs: DEBUG_RESUME\n");
		sendrec(DEBUGGER, &m);
		printf("fs(debug_resume): MESSAGE RETURNED FROM DEBUGGER\n");
		break;
	}
	default:{
		printf("UNRECOGNISED REQUEST\n");
		break;
	}
  }  
  return OK;
}  
#endif
