# compiled from vars
# statically allocated variables here:
	.data
_j:
	.space	4
_i:
	.space	4
_newline:
	.asciiz "\n"	# newline
# start of the generated code
	.text
	.globl	main
main:
	li	$t0,1
	sw	$t0,_j
	lw	$t0,_j
	li	$ ,2
	li	$t2,123
	li	$t3,2
	sub	$t2,$t2,$t3
	mul	$t1,$t1,$t2
	add	$t0,$t0,$t1
	sw	$t0,_i
	li	$t0,123
	sw	$t0,_j
	lw	$a0,_i
	li	$v0,1
	syscall
	la	$a0,_newline
	li	$v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
