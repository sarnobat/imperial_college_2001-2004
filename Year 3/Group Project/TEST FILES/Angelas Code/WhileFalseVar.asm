#Code generated by the lab compiler version 2.1
#Module name WhileFalseVar
	.data
_x:	 pace	4
__newline:
	.asciiz	"\n"
	.text
	.globl main
main:	#first instruction
	li	$a1,1
	sw	$a1,_x
L1:
	lw	$a1,_x
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
