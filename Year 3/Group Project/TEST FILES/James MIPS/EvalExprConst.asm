# compiled from EvalExprConst
# statically allocated variables here:
	.data
_y:
	.spac 4
_newline:
	.asciiz "\n"	# newline
# start of the generated code
	.text
	.globl	main
main:
	li	$t0,12
	mul	$t0,$t0,100
	li	$t1,34
	add	$t0,$t0,$t1
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
