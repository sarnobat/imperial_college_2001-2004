# compiled from WhileIf
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
	blt	$ ,1200,L3
	b	L2
L3:
	li	$t0,60
	sw	$t0,_y
	lw	$t0,_y
	blt	$t0,100,L5
	b	L4
L5:
	lw	$t0,_y
	add	$t0,$t0,40
	sw	$t0,_y
L4:
	lw	$t1,_y
	lw	$t0,_x
	add	$t0,$t0,$t1
	sw	$t0,_x
	b	L1
L2:
L6:
	lw	$t0,_x
	blt	$t0,1230,L8
	b	L7
L8:
	lw	$t0,_x
	add	$t0,$t0,10
	sw	$t0,_x
	b	L6
L7:
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
