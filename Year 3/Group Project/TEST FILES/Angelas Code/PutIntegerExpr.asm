#Code generated by the lab compiler version 2.1
#Module name PutIntegerExpr
	.data
_x: space	4
__newline:
	.asciiz	"\n"
	.text
	.globl main
main:	#first instruction
	li	$a1,4
	sw	$a1,_x
	add	$a0,$a1,1230
	li	$v0,1
	syscall
	la	$a0,__newline
	li	$v0,4
	syscall
#sycall exit to end program
	li	$v0,10
	syscall
