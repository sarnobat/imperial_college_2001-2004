#Code generated by the lab compiler version 2.1
#Module name IfJumpAnd
	.data
_x:	.space	4
__n line:
	.asciiz	"\n"
	.text
	.globl main
main:	#first instruction
	sw	$zero,_x
	lw	$a1,_x
	bgt	$a1,0,L3
	b	L2
L3:
	li	$a3,1
	div	$a2,$a3,$a1
	bgt	$a2,0,L1
	b	L2
L1:
	li	$a0,456
	li	$v0,1
	syscall
	b	L6
L2:
	li	$a0,1234
	li	$v0,1
	syscall
L6:
	la	$a0,__newline
	li	$v0,4
	syscall
#sycall exit to end program
	li	$v0,10
	syscall
