.sect .text; .sect .rom; .sect .data; .sect .bss
.sect .text
.define .unknown
.extern EILLINS, .fat

. known:
	mov  eax,EILLINS
	push eax
	jmp  .fat
