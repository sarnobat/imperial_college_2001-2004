# compiled from WhileLess
# statically allocated variables here:
	.data
_x:
	.space	4
_newline 	.asciiz "\n"	# newline
# start of the generated code
	.text
	.globl	main
main:
	sw	$zero,_x
L1:
	lw	$t0,_x
	blt	$t0,1200,L3
	b	L2
L3:
	lw	$t0,_x
	add	$t0,$t0,100
	sw	$t0,_x
	b	L1
L2:
L4:
	lw	$t0,_x
	blt	$t0,1230,L6
	b	L5
L6:
	lw	$t0,_x
	add	$t0,$t0,10
	sw	$t0,_x
	b	L4
L5:
	lw	$t0,_x
	add	$t0,$t0,4
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
