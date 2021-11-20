	.file	"example.c"
	.version	"01.01"
gcc2_compiled.:
.text
	.align 16
.globl f
	.type	 f,@function
f:
	pushl %ebp
	movl %esp,%ebp
	pushl %ebx
	nop
	movl $1,i
	.p2align 4,,7
.L3:
	cmpl $49,i
	jle .L6
	jmp .L4
	.p2align 4,,7
.L6:
	movl i,%eax
	movl %eax,%edx
	leal 0(,%edx,4),%eax
	movl $a,%edx
	movl 8(%ebp),%ebx
	movl %ebx,%ecx
	addl %ecx,%ecx
	addl %ebx,%ecx
	leal 10(%ecx),%ebx
	movl %ebx,(%eax,%edx)
.L5:
	incl i
	jmp .L3
	.p2align 4,,7
.L4:
.L2:
	movl -4(%ebp),%ebx
	movl %ebp,%esp
	popl %ebp
	ret
.Lfe1:
	.size	 f,.Lfe1-f
	.align 16
.globl main
	.type	 main,@function
main:
	pushl %ebp
	movl %esp,%ebp
	subl $8,%esp
	addl $-12,%esp
	movl b,%eax
	pushl %eax
	call f
	addl $16,%esp
	xorl %eax,%eax
	jmp .L7
	.p2align 4,,7
.L7:
	movl %ebp,%esp
	popl %ebp
	ret
.Lfe2:
	.size	 main,.Lfe2-main
	.comm	i,4,4
	.comm	a,400,32
	.comm	b,4,4
	.ident	"GCC: (GNU) 2.95.3 20010315 (SuSE)"
