# compiled from Repeat
# statically allocated variables here:
	.data
_y:
	.space	4
_x:? .space	4
_newline:
	.asciiz "\n"	# newline
# start of the generated code
	.text
	.globl	main
main:
	li	$t0,4
	sw	$t0,_x
	sw	$zero,_y
L1:
	lw	$t0,_x
	add	$t0,$t0,100
	sw	$t0,_x
	lw	$t0,_y
	add	$t0,$t0,1
	sw	$t0,_y
	lw	$t0,_y
	beq	$t0,12,L2
	b	L1
L2:
	sw	$zero,_y
L3:
	lw	$t0,_x
	add	$t0,$t0,10
	sw	$t0,_x
	lw	$t0,_y
	add	$t0,$t0,1
	sw	$t0,_y
	lw	$t0,_y
	beq	$t0,3,L4
	b	L3
L4:
	lw	$a0,_x
	li	$v0,1
	syscall
	la	$a0,_newline
	li	$v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
