# compiled from IfOrNot
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
	bgt	$t0,41,L3
 	L2
L3:
	lw	$t0,_i
	beq	$t0,98,L1
	b	L2
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
