# compiled from EvalExprUnaryPlus
# statically allocated variables here:
	.dat _x:
	.space	4
_newline:
	.asciiz "\n"	# newline
# start of the generated code
	.text
	.globl	main
main:
	li	$t1,66
	li	$t0,1300
	sub	$t0,$t0,$t1
	sw	$t0,_x
	lw	$a0,_x
	li	$v0,1
	syscall
	la	$a0,_newline
	li	$v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
