# compiled from PutIntegerExpr
# statically allocated variables here:
	.data
_x:
	.spa 	4
_newline:
	.asciiz "\n"	# newline
# start of the generated code
	.text
	.globl	main
main:
	li	$t0,4
	sw	$t0,_x
	lw	$t1,_x
	li	$t0,1230
	add	$t0,$t0,$t1
	move	$a0,$t0
	li	$v0,1
	syscall
	la	$a0,_newline
	li	$v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
