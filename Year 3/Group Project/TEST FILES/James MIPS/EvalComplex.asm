# compiled from EvalExprPlus
# statically allocated variables here:
	.data
_y:
	.space	4
_x:
	 pace	4
_newline:
	.asciiz "\n"	# newline
# start of the generated code
	.text
	.globl	main
main:
	li	$t0,2
	sw	$t0,_y
	lw	$t0,_y
	li	$t1,17
	mul	$t1,$t1,$t0
	lw	$t0,_y
	li	$t2,4
	mul	$t2,$t2,$t0
	mul	$t2,$t2,5
	mul	$t2,$t2,10
	li	$t0,200
	add	$t0,$t0,$t2
	lw	$t2,_y
	mul	$t2,$t2,$t0
	lw	$t0,_y
	add	$t0,$t0,$t2
	add	$t0,$t0,$t1
	sub	$t0,$t0,2
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
