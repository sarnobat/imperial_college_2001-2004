#Code generated by the lab compiler version 2.1
#Module name IfOr
	.data
_i:	.space	4
__newline:
	.asciiz	"\n"
	.text
	.globl main
main:	#first instruction
	li	$a1,43
	sw	$a1,_i
	blt	$a1,41,L1
	b	L3
L3:
	bne	$a1,98,L1
	b	L L1:
	li	$a0,1234
	li	$v0,1
	syscall
	b	L6
L2:
	li	$a0,456
	li	$v0,1
	syscall
L6:
	la	$a0,__newline
	li	$v0,4
	syscall
#sycall exit to end program
	li	$v0,10
	syscall
