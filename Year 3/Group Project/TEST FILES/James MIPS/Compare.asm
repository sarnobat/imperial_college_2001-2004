# compiled from Compare
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
	beq	$t0,$zero,L2
	b	L1
L2:
	li	$t0,2
	sw	$t0,_x
	b	L3
L1:
	li	$t0,1
	sw	$t0,_x
L3:
	lw	$t0,_x
	bgt	$t0,$zero,L5
	b	L4
L5:
	li	$t0,34
	sw	$t0,_x
	b	L6
L4:
	li	$t0,2
	sw	$t0,_x
L6:
	lw	$t0,_x
	blt	$t0,40,L8
	b	L7
L8:
	lw	$t1,_x
	li	$t0,200
	add	$t0,$t0,$t1
	sw	$t0,_x
	b	L9
L7:
	li	$t0,3
	sw	$t0,_x
L9:
	lw	$t0,_x
	bge	$t0,200,L11
	b	L10
L11:
	lw	$t0,_x
	add	$t0,$t0,1000
	sw	$t0,_x
	b	L12
L10:
	li	$t0,4
	sw	$t0,_x
L12:
	lw	$t0,_x
	bne	$t0,$zero,L14
	b	L13
L14:
	lw	$t0,_x
	add	$t0,$t0,1
	sw	$t0,_x
	b	L15
L13:
	li	$t0,5
	sw	$t0,_x
L15:
	lw	$t0,_x
	ble	$t0,2000,L17
	b	L16
L17:
	lw	$t0,_x
	sub	$t0,$t0,1
	sw	$t0,_x
	b	L18
L16:
	li	$t0,6
	sw	$t0,_x
L18:
	lw	$a0,_x
	li	$v0,1
	syscall
	la	$a0,_newline
	li	$v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
