	.file	"example.c"
	.version	"01.01"
gcc2_compiled.:
	.comm	i,4,4
	.comm	a,400,32
	.comm	b,4,4
.text
	.align 16
.globl f
	.type	 f,@function
f:
	pushl %ebp
	movl %esp,%ebp
	movl $a,%ecx
	movl 8(%ebp),%eax
	movl $1,i
	leal 10(%eax,%eax,2),%edx
	.p2align 4,,7
.L6:
	movl i,%eax
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
	movl b,%eax
	movl $1,i
	leal 10(%eax,%eax,2),%edx
	.p2align 4,,7
.L11:
	movl i,%eax
	movl %edx,a(,%eax,4)
	incl %eax
	movl %eax,i
	cmpl $49,%eax
	jle .L11
	xorl %eax,%eax
	movl %ebp,%esp
	popl %ebp
	ret
.Lfe2:
	.size	 main,.Lfe2-main
	.ident	"GCC: (GNU) 2.95.3 20010315 (SuSE)"
