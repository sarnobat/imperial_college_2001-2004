# compiled from Instruction
# statically allocated variables here:
	.data
_b:
	.space	4
_j:
	. ace	4
_newline:
	.asciiz "\n"	# newline
# start of the generated code
	.text
	.globl	main
main:
	li	$t0,11105
	sw	$t0,_b
	li	$t0,1
	sw	$t0,_j
	lw	$t1,_j
	lw	$t0,_b
	add	$t0,$t0,$t1
	sw	$t0,_b
	lw	$t0,_b
	div	$t0,$t0,9
	move	$a0,$t0
	li	$v0,1
	syscall
	la	$a0,_newline
	li	$v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
