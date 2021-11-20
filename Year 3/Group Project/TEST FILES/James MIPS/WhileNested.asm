# compiled from WhileNested
# statically allocated variables here:
	.data
_y:
	.space	4
_x:
	.space	4
_newline:
	.asciiz "\n"	# newline
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
	li	$t0,60
	sw	$t0,_y
L4:
	lw	$t0,_y
	blt	$t0,100,L6
	b	L5
L6:
	lw	$t0,_y
	add	$t0,$t0,20
	sw	$t0,_y
	b	L4
L5:
	lw	$t1,_y
	lw	$t0,_x
	add	$t0,$t0,$t1
	sw	$t0,_x
	b	L1
L2:
L7:
	lw	$t0,_x
	blt	$t0,1230,L9
	b	L8
L9:
	lw	$t0,_x
	add	$t0,$t0,10
	sw	$t0,_x
	b	L7
L8:
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
