# compiled from RepeatJumpAnd
# statically allocated variables here:
	.data
_x:
	.spac 4
_newline:
	.asciiz "\n"	# newline
# start of the generated code
	.text
	.globl	main
main:
	li	$t0,1
	neg	$t0,$t0
	sw	$t0,_x
L1:
	lw	$t0,_x
	add	$t0,$t0,1
	sw	$t0,_x
	lw	$t0,_x
	bgt	$t0,$zero,L3
	b	L1
L3:
	lw	$t1,_x
	li	$t0,1
	div	$t0,$t0,$t1
	bgt	$t0,$zero,L2
	b	L1
L2:
	li	$a0,1234
	li	$v0,1
	syscall
	la	$a0,_newline
	li	$v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
