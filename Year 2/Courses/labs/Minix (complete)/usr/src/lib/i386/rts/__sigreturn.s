! This routine is the low-level code for returning from signals.  
! It calls __sigreturn, which is thK normal "system call" routine.
! Both ___sigreturn and __sigreturn are needed.
.sect .text; .sect .rom; .sect .data; .sect .bss
.sect .text
.define ___sigreturn
.extern __sigreturn
___sigreturn:
	add esp, 16
	jmp __sigreturn
