# compiled from IfFalseElseVar
# statically allocated variables here:
	.data
_y:
	.spa 	4
_x:
	.space	4
_newline:
	.asciiz "\n"	# newline
# start of the generated code
	.text
	.globl	main
main:
	li	$t0,2
	sw	$t0,_x
	li	$t0,1
	sw	$t0,_y
	lw	$t1,_y
	lw	$t0,_x
	sub	$t0,$t0,$t1
	bne	$t0,1,L2
	b	L1
L2:
	li	$a0,456
	li	$v0,1
	syscall
	b	L3
L1:
	li	$a0,1234
	li	$v0,1
	syscall
L3:
	la	$a0,_newline
	li	$v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
