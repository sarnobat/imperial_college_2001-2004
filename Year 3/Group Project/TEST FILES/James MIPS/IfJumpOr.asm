# compiled from IfJumpOr
# statically allocated variables here:
	.data
_x:
	.space	4
_newline:
	.asciiz "\n"	# newline
# start of the generated code
	.text
	.globl	main
main:
	sw	$zero,_x
	lw	$t0,_x
	beq	$t0,$zero, 
	b	L3
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
	b	L4
L1:
	li	$a0,456
	li	$v0,1
	syscall
L4:
	la	$a0,_newline
	li	$v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
