#Code generated by the lab compiler version 2.1
#Module name EvalExprPlusVar
	.data
_y .space	4
_x:	.space	4
__newline:
	.asciiz	"\n"
	.text
	.globl main
main:	#first instruction
	li	$a1,34
	sw	$a1,_y
	add	$a2,$a1,1200
	sw	$a2,_x
	move	$a0,$a2
	li	$v0,1
	syscall
	la	$a0,__newline
	li	$v0,4
	syscall
#sycall exit to end program
	li	$v0,10
	syscall
