# compiled from IfAnd
# statically allocated variables here:
	.data
_i:
	.space	4
_newline:
	.asciiz "\n"	# newline
# start of the generated code
	.text
	.globl	main
main:
	li	$t0,43
	sw	$t0,_i
	lw	$t0,_i
	blt	$t0,44,L3
	b 1
L3:
	lw	$t0,_i
	bne	$t0,98,L2
	b	L1
L2:
	li	$a0,1234
	li	$v0,1
	syscall
	b	L4
L1:
	li	$a0,456
	li	$v0,1
	syscall
L4:
	la	$a0,_newline
	li	$v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
