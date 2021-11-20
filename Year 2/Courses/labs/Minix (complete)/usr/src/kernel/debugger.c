/* This file contains a debugger.
 * Debbuging of user programs is started by the command 'debug fn'  
 * which results in a message to this driver with type "START"
 */

#include "kernel.h"
#include <stdlib.h>
#include <signal.h>
#include <minix/callnr.h>
#include <minix/com.h>
#include "proc.h"
 
#if ENABLE_DEBUGGING

#define BREAKPOINT_LENGTH 1	/* # of bytes for a breakpoint */
#define PROC_LENGTH sizeof (struct proc)

PRIVATE char break_code = 0xCC; /* this code causes an interrupt */
PRIVATE char store_code;	/* to save the code that was originally
				 * in the byte */

typedef struct {
	int proc_nr;
	int defined;
	char *address;
	phys_bytes vir;
	struct proc *rp;
	long brkadd;
} users;

PRIVATE users parent, child;

PRIVATE int interrupt_no; /* used to register the interrupt */
PRIVATE phys_bytes addr_umap;
PRIVATE	phys_bytes store_addr;
FORWARD _PROTOTYPE( void do_cancel, (message *cancel_m)			);
FORWARD _PROTOTYPE( void do_start, (message *instr_m)			);
FORWARD _PROTOTYPE( void do_interrupt, (void)				);
FORWARD _PROTOTYPE( void do_mourn, (message *mourn_m)			);
FORWARD _PROTOTYPE( void do_evaluate, (message *instr_m)		);

FORWARD _PROTOTYPE( void do_resume, (message *instr_m)			);
FORWARD _PROTOTYPE( void do_step, (message *instr_m)			);
FORWARD _PROTOTYPE( void do_breakpnt, (message *instr_m)		);
FORWARD _PROTOTYPE( void do_procadm, (message *instr_m)			);

FORWARD _PROTOTYPE( void breakpoint_int, (void)				); 
FORWARD _PROTOTYPE( void step_int, (void)				);

FORWARD _PROTOTYPE( void fs_reply, (int code, int proc_nr, int status)	); 
FORWARD _PROTOTYPE( void mm_reply, (int status)				); 
FORWARD _PROTOTYPE( void init_debug, (void)				); 

/*==========================================================================*
 *			fs_reply && mm_reply				    *
 *==========================================================================*/

/* Builds message, sets variables and sends it to fs or mm tasks */

PRIVATE void fs_reply(int code, int proc_nr, int status){
	message fs_mess;

	fs_mess.m_type = code;
	fs_mess.REP_STATUS = status;
	fs_mess.REP_PROC_NR = proc_nr;

	send(FS_PROC_NR, &fs_mess);
}

PRIVATE void mm_reply(int status){
	message mm_mess;

	mm_mess.REP_STATUS = status;
	printf("MM Reply USED\n");

	send(MM_PROC_NR, &mm_mess);
}

/*==========================================================================*
 *				 debug_task				    *
 *==========================================================================*/
PUBLIC void debug_task()
{
/* Main routine of the debugger. Here messages are received and handled. 
 * CANCEL	The termination of either the process to be debugged or 
 *		the parent.
 * CHILD_DIED	MM informs that the child has terminated.
 * DEBUG_START	MM informs the the child has done an exec. 
 * HARD_INT	Hardware received an interrupt.
 * INSTRUCTION	The parent sent a message. 
 */
  message debug_m;

  init_debug();

  while(TRUE) {
	receive(ANY, &debug_m);

	switch(debug_m.m_type){
	case CANCEL:		do_cancel(&debug_m);	break;
	case CHILD_DIED:	do_mourn(&debug_m);	break;
	case DEBUG_START:	do_start(&debug_m);	break;
	case HARD_INT:		do_interrupt();		break;
	case INSTRUCTION:	do_evaluate(&debug_m);	break;
	default:{ 
		if (debug_m.m_source == FS_PROC_NR){ 
			fs_reply (TASK_REPLY, debug_m.PROC_NR, ENOSYS);
		}
		else {
			mm_reply (ENOSYS);
		}
		break;
	}
} }	}

/*==========================================================================*
 *				 do_cancel				    *
 *==========================================================================*/
PRIVATE void do_cancel(message *cancel_m)
{
/* Deal with the arrival of a CANCEL message from the kill action when you
   type "kill" at the konsole" */

	init_debug(); /* defined below, kills child and parent processes */

	if(cancel_m -> m_source == FS_PROC_NR){
		fs_reply(TASK_REPLY, cancel_m -> PROC_NR, OK);
	}
	else { /* Case when MM_PROC_NR */
		mm_reply(EINTR); /* replies to mm with an error message */
	}
}
/*==========================================================================*
 *				 do_start				    *
 *==========================================================================*/
PRIVATE void do_start (message *start_m)
{
/* By use of the system call debug a request has been made to debug a
 * program, with the effect that a bit was raised in the flags.
 * This program then called exec; this was caught by MM, which sent a 
 * message to DEBUGGER.
 * If the parent is already known to the system, send the child's process
 * descriptor over; otherwise, do nothing now.
 * Reply to caller (MM) that everything went well. 
 */
	child.defined = TRUE;
	/* child is now created */

	child.proc_nr = start_m -> PROC_NR;
	/* saves the childs process no. */

	child.rp = proc_addr(child.proc_nr);
	/* child.rp is the pointer to the child's process descriptor */ 

	child.vir = vir2phys(child.rp);
  	/* sets child's physical source address */

	printf("debugger.c: CHILD REMOVED FROM READY QUEUE\n");

	lock_unready(child.rp);
	/* takes the child off the ready queue */

	mm_reply(OK);

	if(parent.defined){
		
   		printf("before copy resumed from do_procadm(): in do_start()");
		printf("\n");
		
		phys_copy(child.vir, parent.vir, (phys_bytes) PROC_LENGTH);
		/* copies the child source address to parent */

		printf("after copy resumed from do_procadm(): in do_start()");
		printf("\n");
		
		fs_reply(REVIVE, parent.proc_nr, OK);
		/* tells fs to revive parent */

	}
} 

 /*==========================================================================*
 *				 do_mourn				    *
 *==========================================================================*/
PRIVATE void do_mourn (message *mourn_m)
{
/* MM has reported the death of the child. Wake up the parent. */
	
	/* Stage 9 */
	
	child.defined = FALSE;
	/* kills the child process */
	fs_reply(REVIVE, parent.proc_nr, ECHILD);
	/* revives the parent */
	
	mm_reply(OK);

}

/*==========================================================================*
 *				do_evaluate				    *
 *==========================================================================*/
PRIVATE void do_evaluate (message *instr_m)
{
/* Evaluate the INSTRUCTION that arrived from the parent */

  switch(instr_m -> REQUEST){
	case DEBUG_RESUME:	do_resume(instr_m);	break;
	case DEBUG_STEP:	do_step(instr_m);	break;
	case DEBUG_BREAKPNT:	do_breakpnt(instr_m);	break;
	case DEBUG_PROCADM:	do_procadm(instr_m);	break;
	default:					break;
  }
}

/*==========================================================================*
 *				do_resume				    *
 *==========================================================================*/
PRIVATE void do_resume (message *resume_m)	/* resume execution */
{
/* Let the child run to completion; the parent will terminate as well
   because it calls wait_pid (it is supposed to) */

	/* Stage 7 */
	
	child.rp -> p_reg.psw &= !TRACEBIT;
	/* lowering the tracebit */

	lock_ready(child.rp);
	/* put child on ready queue */

	fs_reply(TASK_REPLY, parent.proc_nr, OK);

}

/*==========================================================================*
 *				   do_step				    *
 *==========================================================================*/
PRIVATE void do_step (message *step_m) 
{
  child.rp -> p_reg.psw |= TRACEBIT;
  /* sets TRACEBIT flag (using bitwise OR) in PSW of child*/

  printf("TRACEBIT RAISED\n");

  lock_ready(child.rp);
  /* adds the child to the ready queue, to run */

  fs_reply(TASK_REPLY, parent.proc_nr, SUSPEND);
  /* parent wants to display the info, but before it can do that,
     suspend parent before child is put on the ready queue */
}

/*==========================================================================*
 *				 do_breakpnt				    *
 *==========================================================================*/
PRIVATE void do_breakpnt (message *break_m)	/* set a breakpoint */
{
/* Place a breakpoint at the break_m->POSITION in the user space */

	/* Stage 8 */

	printf("Position is %s",break_m->POSITION);
	printf("Source is %s", break_m->m_source);
	printf("Type is %s", break_m->m_type);
	printf("Request is %s", break_m->REQUEST);

  if(child.defined){
	
	phys_bytes breakpoint_addr; 
	/* physical addr of break code*/ 
	long child_brk_pos; 
	/* position of where to insert breakpoint, relative to the start of the child code */ 
	
	breakpoint_addr = vir2phys(&break_code); 
	/* sets breakpoint_addr */ 
	store_addr = vir2phys(&store_code);
	/* sets position of where to save child code */ 

	child_brk_pos = break_m -> POSITION; 
	/* sets where to insert brkpnt */

	addr_umap = umap(child.rp, T, child_brk_pos, BREAKPOINT_LENGTH);
	/* calcultes pysical memory address */

	phys_copy(addr_umap, store_addr, BREAKPOINT_LENGTH);
	phys_copy(breakpoint_addr, addr_umap, BREAKPOINT_LENGTH);

	fs_reply(TASK_REPLY, parent.proc_nr, SUSPEND);

	child.rp -> p_reg.psw &= ~TRACEBIT;

	lock_ready(child.rp);
  }
  else{
	fs_reply(TASK_REPLY, parent.proc_nr, ECHILD);
  }
}

/*==========================================================================*
 *				do_procadm				    *
 *==========================================================================*/
PRIVATE void do_procadm (message *proc_m)
{
/* The parent makes contact for the first time; if the child is known, copy 
 * the process administration of the child to the parent */

  /* stage 4 */
  
  parent.defined = TRUE;
  /* the parent is now alive */

  parent.proc_nr = proc_m -> PROC_NR;
  /* Save parent process number for future use and suspend */

  parent.rp = proc_addr(parent.proc_nr);
  /* pointer to the parent's process descriptor */

  parent.vir = proc_vir2phys(parent.rp, proc_m -> ADDRESS);
  /* proc_m -> ADDRESS is the address relative to the start of that text 
     segment (in parent.rp: debug.c). p_v2p() calc. addr of where to put info
     in user space (of the parent) */  

  if(child.defined){
	phys_copy(child.vir, parent.vir, (phys_bytes) PROC_LENGTH);
	printf("CHILD COPIED TO PARENT");
	fs_reply(TASK_REPLY, parent.proc_nr, OK);
  }
  else{
	fs_reply(TASK_REPLY, parent.proc_nr, SUSPEND);
  }
}

/*==========================================================================*
 *				do_interrupt				    *
 *==========================================================================*/
PRIVATE void do_interrupt()
{
/* An interrupt has occurred. Figure out what has happened, and act
 * accordingly. */

/* stage 6 */
  if(interrupt_no == SINGLE_STEP){
	step_int();
  }
  if(interrupt_no == BREAKPOINT){
	breakpoint_int(); 
  }
}

/*==========================================================================*
 *				breakpoint_int				    *
 *==========================================================================*/
PRIVATE void breakpoint_int(void)
{
/* The process to be debugged has reached an breakpoint. Restore the 
 * original code and reset the program counter. Inform the parent. */

  printf("in debugger.c: breakpoint_int()\n");
  
  /* Stage 8 */
  lock_unready(child.rp);

  phys_copy(store_addr, addr_umap, BREAKPOINT_LENGTH);

  (child.rp -> p_reg.pc)--;

  phys_copy(child.vir, parent.vir, (phys_bytes) PROC_LENGTH);

  printf("CHILD COPIED TO PARENT in breakpoint_int()\n");

  fs_reply(REVIVE, parent.proc_nr, OK);

}

/*==========================================================================*
 *				 step_int				    *
 *==========================================================================*/
PRIVATE void step_int(void)
{
/* The process to be debugged has performed an assembler instruction.
 * Inform the parent. */

  printf("debugger.c:step_int()\n");
	
  lock_unready(child.rp);
  /* takes the child off the ready queue */

  if(parent.defined){
	phys_copy(child.vir, parent.vir, (phys_bytes) PROC_LENGTH);

	printf("CHILD COPIED TO PARENT\n");
	fs_reply(REVIVE, parent.proc_nr, OK);
  }
}

/*==========================================================================*
 *				breakpoint				    *
 *==========================================================================*/
PUBLIC int breakpoint()
{
/* The program that is debugged has reached an breakpoint. Stop the 
 * execution of the process and inform the debugger */

/* Stage 8 */
  interrupt_no = BREAKPOINT;
  interrupt(DEBUGGER);
}

/*==========================================================================*
 *				  single_step				    *
 *==========================================================================*/
PUBLIC int single_step()
{
/* The program that is debugged has reached a step interrupt. 
 * Stop the execution of the process and inform the debugger */
/* LOOK IN proc.c in kernel for method interrupt */

/* stage 6 */
  interrupt_no = SINGLE_STEP;
  interrupt(DEBUGGER);
}

/*==========================================================================*
 *				  init_debug				    *
 *==========================================================================*/
PRIVATE void init_debug()
{
	/* resets main data structure variables */
	parent.defined = FALSE;
	child.defined = FALSE;	
}

#endif /* ENABLE_DEBUGGING */
