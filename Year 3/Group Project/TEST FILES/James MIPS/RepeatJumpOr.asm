# compiled from RepeatJumpOr
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
	li	$a0,1234
	li	$v0,1
	syscall
	lw	$t0,_x
	beq	$t0,$zero,L2
	b	L3
L3:
	lw	$t1,_x
	li	$t0,1
	div	$t0,$t0,$t1
	beq	$t0,$zero,L2
	b	L1
L2:
	la	$a0,_newline
	li	$v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
