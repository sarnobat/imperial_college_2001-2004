# compiled from EvalConstExpr
# statically allocated variables here:
	.data
_x:
	.spac 4
_newline:
	.asciiz "\n"	# newline
# start of the generated code
	.text
	.globl	main
main:
	li	$t1,1
	add	$t1,$t1,9
	li	$t0,1
	mul	$t0,$t0,10
	add	$t0,$t0,2
	mul	$t0,$t0,$t1
	add	$t0,$t0,3
	li	$t1,2
	add	$t1,$t1,8
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
