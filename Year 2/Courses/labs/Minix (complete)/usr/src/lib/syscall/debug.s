.sect .text
.extern	__debug
.define	_debug
.align 2

_debug:
	jmp	__debug
