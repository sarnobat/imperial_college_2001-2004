# compiled from EvalExpr
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
	li	$t0,10
	sw	$t0,_x
	lw	$t1,_x
	li	$t 1
	mul	$t0,$t0,$t1
	add	$t0,$t0,2
	lw	$t1,_x
	mul	$t0,$t0,$t1
	add	$t0,$t0,3
	lw	$t1,_x
	mul	$t0,$t0,$t1
	add	$t0,$t0,4
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
