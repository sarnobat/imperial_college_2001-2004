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
	movl 8(%ebp),%eax
	movl $1,i
	movl $a,%ecx
	leal (%eax,%eax,2),%eax
	leal 10(%eax),%edx
	.p2align 4,,7
.L6: movl i,%eax
	movl %edx,(%ecx,%eax,4)
	incl %eax
	movl %eax,i
	cmpl $49,%eax
	jle .L6
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
	xorl %eax,%eax
	movl %ebp,%esp
	popl %ebp
	ret
.Lfe2:
	.size	 main,.Lfe2-main
	.comm	i,4,4
	.comm	a,400,32
	.comm	b,4,4
	.ident	"GCC: (GNU) 2.95.3 20010315 (SuSE)"
