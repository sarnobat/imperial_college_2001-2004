	.file	"cversion.c"
	.version	"01.01"
gcc2_compiled.:
.globl message
.data
	.type	 message,@object
	.size	 message,14
message:
	.string	"Hello World!\n"
.text
	.align 16
.globl main
	.type	 main,@function
main:
	pushl %ebp movl %esp,%ebp
	subl $8,%esp
	addl $-4,%esp
	pushl $13
	pushl $message
	pushl $1
	call write
	addl $16,%esp
	addl $-12,%esp
	pushl $1
	call exit
	addl $16,%esp
	.p2align 4,,7
.L2:
	movl %ebp,%esp
	popl %ebp
	ret
.Lfe1:
	.size	 main,.Lfe1-main
	.ident	"GCC: (GNU) 2.95.3 20010315 (SuSE)"
