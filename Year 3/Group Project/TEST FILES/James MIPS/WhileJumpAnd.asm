# compiled from WhileJumpAnd
# statically allocated variables here:
	.data
_x:
	.space 
_newline:
	.asciiz "\n"	# newline
# start of the generated code
	.text
	.globl	main
main:
	sw	$zero,_x
L1:
	lw	$t0,_x
	bgt	$t0,$zero,L4
	b	L2
L4:
	lw	$t1,_x
	li	$t0,1
	div	$t0,$t0,$t1
	bgt	$t0,$zero,L3
	b	L2
L3:
	li	$a0,456
	li	$v0,1
	syscall
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
