#Code generated by the lab compiler version 2.1
#Module name AssignConst
	.data
_x:	.space	4
_ ewline:
	.asciiz	"\n"
	.text
	.globl main
main:	#first instruction
	li	$a1,1234
	sw	$a1,_x
	move	$a0,$a1
	li	$v0,1
	syscall
	la	$a0,__newline
	li	$v0,4
	syscall
#sycall exit to end program
	li	$v0,10
	syscall
