# compiled from IfNot
# statically allocated variables here:
	.data
_x:
	.space	4
_newline:
	.asciiz "\n"	# newline
# start of the generated code
	.text
	.globl	main
main:
	li	$t0,20
	sw	$t0,_x
	lw	$t0,_x
	bgt	$t0,20,L1
	b 2
L2:
	li	$a0,1234
	li	$v0,1
	syscall
	b	L3
L1:
	lw	$a0,_x
	li	$v0,1
	syscall
L3:
	la	$a0,_newline
	li	$v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
