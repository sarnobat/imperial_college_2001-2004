.sect .text; .sect .rom; .sect .data; .sect .bss
.sect .text
.define .isar

.isar:
	pop     ecx
	pop     eax
	cmp     eax,4
.extern .unknown
	jne     .unknown
	pop     ebx      ! descriptor address
	pop     eax      ! inde 	push    ecx
.extern .sar4
	jmp    .sar4
