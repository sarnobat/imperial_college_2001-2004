#Code generated by the lab compiler version 2.1
#Module name WhileFalse
	.data
__newline:
	.as iz	"\n"
	.text
	.globl main
main:	#first instruction
L1:
	li	$a1,1
	bne	$a1,1,L3
	b	L2
L3:
	li	$a0,456
	li	$v0,1
	syscall
	b	L1
L2:
	li	$a0,1234
	li	$v0,1
	syscall
	la	$a0,__newline
	li	$v0,4
	syscall
#sycall exit to end program
	li	$v0,10
	syscall
