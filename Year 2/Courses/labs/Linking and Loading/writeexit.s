	.text
# an implementation of write and exit
        .globl write	 
write:	
        pushl  %ebx
        movl  F x10(%esp,1),%edx
        movl   0xc(%esp,1),%ecx
        movl   0x8(%esp,1),%ebx
        movl   $0x4,%eax
        int    $0x80
        popl   %ebx
        ret
	.globl exit
 exit:
        movl   %ebx,%edx
        movl   0x4(%esp,1),%ebx
        movl   $0x1,%eax
        int    $0x80
	.globl _start
# At the program start jump to the C function main
_start:
	jmp	main
	
