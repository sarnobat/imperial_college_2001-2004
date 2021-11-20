# compiled from square
# statically allocated variables here:
	.data
_j:
	.space	4
_i:
	.space	4
_newline:
	.asciiz "\n"	# newline
# start of the generated code
	.text
	.globl	main
main:
	li	$t0,1
	sw	$t0,_i
L1:
	lw	$t0,_i li	$t1,11
	ble	$t0,$t1,L3
	b	L2
L3:
	li	$t0,1
	sw	$t0,_j
L4:
	lw	$t0,_j
	li	$t1,11
	blt	$t0,$t1,L6
	b	L5
L6:
	lw	$t0,_j
	lw	$t1,_i
	blt	$t0,$t1,L8
	b	L7
L8:
	li	$a0,0
	li	$v0,1
	syscall
	b	L9
L7:
	li	$a0,1
	li	$v0,1
	syscall
L9:
	lw	$t0,_j
	add	$t0,$t0,1
	sw	$t0,_j
	b	L4
L5:
	lw	$t0,_i
	add	$t0,$t0,1
	sw	$t0,_i
	la	$a0,_newline
	li	$v0,4
	syscall
	b	L1
L2:
# exit call to stop the program
	li	$v0,10
	syscall
