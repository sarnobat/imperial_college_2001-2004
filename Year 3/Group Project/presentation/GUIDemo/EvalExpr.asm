#Code generated by the lab compiler version 2.1
#Module name EvalExpr
	.data
_x:	.space	4
__newline:
	.asciiz	"\n"
	.text
	.globl main
main:	#first instruction
	li	$a1,10
	sw	$a1,_x
	mul	$a2,$a1,1
	add	 3,$a2,2
	mul	$a2,$a3,$a1
	add	$a3,$a2,3
	mul	$a2,$a3,$a1
	add	$a1,$a2,4
	sw	$a1,_x
	move	$a0,$a1
	li	$v0,1
	#!!!!!!!ABOUT TO PRINT TO CONSOLE!!!!!!!
	syscall
	la	$a0,__newline
	li	$v0,4
	syscall
#sycall exit to end program
	li	$v0,10
	syscall
