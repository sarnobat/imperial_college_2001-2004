# compiled from WhileJumpOr
# statically allocated variables here:
	.data
_x:
	.space	4
_newli :
	.asciiz "\n"	# newline
# start of the generated code
	.text
	.globl	main
main:
	sw	$zero,_x
L1:
	lw	$t0,_x
	beq	$t0,$zero,L3
	b	L4
L4:
	lw	$t1,_x
	li	$t0,1
	div	$t0,$t0,$t1
	beq	$t0,$zero,L3
	b	L2
L3:
	li	$a0,1234
	li	$v0,1
	syscall
	li	$t0,1
	sw	$t0,_x
	b	L1
L2:
	la	$a0,_newline
	li	$v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
