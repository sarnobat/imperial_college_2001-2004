# compiled from EvalExprPlusVar
# statically allocated variables here:
	.data
_y:
	.sp e	4
_x:
	.space	4
_newline:
	.asciiz "\n"	# newline
# start of the generated code
	.text
	.globl	main
main:
	li	$t0,34
	sw	$t0,_y
	lw	$t1,_y
	li	$t0,1200
	add	$t0,$t0,$t1
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
