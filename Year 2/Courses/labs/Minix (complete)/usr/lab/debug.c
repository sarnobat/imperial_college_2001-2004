/*
 *   command debug written by Steffen van Bakel, Imperial College, London
 *
 *   The debug command takes its first argument and performs the 
 *   'fork' systemcall.  The child will then call the system call 
 *   'debug', which will inform the DEBUGGER of its coming into 
 *   existence, and allow debugging.
 *
 *   London, December 29, 2000
 */

#include <sys/wait.h>
#include <fcntl.h>
#include <stdlib.h>
#include <unistd.h>
#include <curses.h>
#include <errno.h>

#include "../src/kernel/kernelh.h"
#include <minix/com.h>
#include "../src/kernel/proc.h"
#include "../src/lib/posix/_debug.c"

/* _PROTOTYPE( int debug, (char * a, int b, long c)); */
_PROTOTYPE( int main, (int argc, char **argv));
_PROTOTYPE( void show_proc_adm, (void));
_PROTOTYPE( void interact, (void));
_PROTOTYPE( void call_debug, (int instruction));
_PROTOTYPE( void do_not_debugging, (void));
_PROTOTYPE( void do_step_mode, (void));
_PROTOTYPE( void do_break_mode, (void));
_PROTOTYPE( void get_instr, (void));
_PROTOTYPE( void set_breakpoint, (void));
_PROTOTYPE( void finish, (void));
_PROTOTYPE( void error, (char *format, ...));

struct proc proc_adm;
int mode, pid, status, command, request;
long break_addr = 0;

int main(int argc, char **argv)
{
  pid = fork();
  switch (pid) {
   case -1: error("fork failed.\n"); break;
   case 0:
	call_debug (DEBUG_START);
	if (execv(argv[1],argv) != 0)  error("Unable to exec.\n");
   default:
	mode = NOT_DEBUGGING;
	request = DEBUG_PROCADM;
	while(TRUE) {
		call_debug (request);	/* this is where the work is done */
		show_proc_adm(); 
		switch (mode) {
		 case NOT_DEBUGGING:	do_not_debugging();	break;
		 case STEP_MODE:	do_step_mode();		break;
		 case BREAK_MODE:	do_break_mode();
} }	}	}

void do_not_debugging(void)
{
  printf("mode is not_debugging\n\
  switch to step mode:	type 1<return>\n\
  set breakpoint:	type 2<return>\n\
  resume program:	type 3<return>\n");

  while (TRUE) {
	get_instr();
	switch (command) {
	 case 1:	mode = STEP_MODE; 
			printf("Switching to step_mode.\n");
			request = DEBUG_STEP;		return;
	 case 2:	set_breakpoint();		return;
	 case 3:	finish();			return;
	 default:	printf("Try again.\n");	break;
} }	}

void do_step_mode(void)
{
  printf("mode is step_mode\n\
  continue step mode:			type 1<return>\n\
  stop step mode, set breakpoint:	type 2<return>\n\
  stop step mode and resume program:	type 3<return>\n");

  while (TRUE) {
	get_instr();
	switch (command) {
	 case 1:	request = DEBUG_STEP;		return;
	 case 2:	set_breakpoint();		return;
	 case 3:	finish();			return;
	 default:	printf("Try again.\n");	break;
} }	}

void do_break_mode(void)
{
  printf("mode is break_mode\n\
  switch to step mode:	type 1<return>\n\
  set breakpoint:	type 2<return>\n\
  resume program:	type 3<return>\n");

  while (TRUE) {
	get_instr();
	switch (command) {
	 case 1:	mode = STEP_MODE;
			printf("Switching to step_mode.\n");
			request = DEBUG_STEP;		return;
	 case 2:	set_breakpoint();		return;
	 case 3:	finish();			return;
	 default: printf("Try again.\n");	break;
} }	}

void show_proc_adm(void)
{
  printf("\n\nCurrent (hex) values in the registers:\n");	
#if _WORD_SIZE == 4
  printf("	GS: %04x	FS: %04x",
	proc_adm.p_reg.gs, proc_adm.p_reg.fs);
#endif
  printf("	ES: %04x\n",	proc_adm.p_reg.es);
  printf("	DS: %04x,	DI: %04x,	SI: %04x\n",
	proc_adm.p_reg.ds, proc_adm.p_reg.di, proc_adm.p_reg.si);
  printf("	FP: %04x,	ST: %04x,	BX: %04x\n",
	proc_adm.p_reg.fp, proc_adm.p_reg.st, proc_adm.p_reg.bx);
  printf("	DX: %04x,	CX: %04x\n",
	proc_adm.p_reg.dx, proc_adm.p_reg.cx);
  printf("	Return register: %04x,	Return address : %04x\n",
	proc_adm.p_reg.retreg, proc_adm.p_reg.retadr);
  printf("	program counter: %04x,	program status : %04x\n",
	proc_adm.p_reg.pc, proc_adm.p_reg.psw);
  printf("	cs             : %04x,	stack pointer  : %04x\n",
	proc_adm.p_reg.cs, proc_adm.p_reg.sp);
  printf("	process id (dec)    : %04d\n\n", proc_adm.p_nr);
}

void finish(void)
{
  printf("Finishing debugging ... ");
  mode = NOT_DEBUGGING;
  call_debug (DEBUG_RESUME);
  waitpid(pid, &status, 0);
  error ("Child process has finished.\n");
}

void set_breakpoint(void)
{
  printf("Give breakpoint address: ");
  scanf("%x",&break_addr);
  printf("Breakpoint to be placed at address %d (decimal) \n", break_addr);
  mode = BREAK_MODE;  
  request = DEBUG_BREAKPNT;
}

void call_debug (int instruction)
{
  if (debug ((char *)&proc_adm, instruction, break_addr) != OK) 
	if (errno == ECHILD) error ("Child process has terminated.\n");
	else error ("System call debug failed.\n");
}

void get_instr(void)
{
  printf("\nInstruction: "); 
  scanf("%d", &command);
/*  printf("Instruction typed: %d\n", command); */
}

void error (char *format, ...)
{
  printf(format);
  exit(-1);
}
