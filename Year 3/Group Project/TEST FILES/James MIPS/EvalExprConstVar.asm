# compiled from EvalExprConstVar
# statically allocated variables here:
	.data y:
	.space	4
_x:
	.space	4
_newline:
	.asciiz "\n"	# newline
# start of the generated code
	.text
	.globl	main
main:
	li	$t0,12
	sw	$t0,_x
	lw	$t0,_x
	mul	$t0,$t0,100
	add	$t0,$t0,34
	sw	$t0,_y
	lw	$a0,_y
	li	$v0,1
	syscall
	la	$a0,_newline
	li	$v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
