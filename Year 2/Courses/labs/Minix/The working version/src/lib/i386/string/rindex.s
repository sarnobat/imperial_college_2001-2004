!	rindex()					Author: Kees J. Bot
!								2 Jan 1994
.sect .text; .sect .rom; .sect .data; .sect .bss

! char *rindex(const char *s, int c)
!	Look for the last occurrence a character in a string.  Has suffered
!	from a h�!tile takeover by strrchr().
!
.sect .text
.define _rindex
	.align	16
_rindex:
	jmp	_strrchr