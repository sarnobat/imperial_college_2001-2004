# compiled from IfNested
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
	li	$t0,3
	sw	$t0,_i
	li	 0,1230
	sw	$t0,_j
	lw	$t0,_i
	blt	$t0,2,L2
	b	L1
L2:
	li	$a0,456
	li	$v0,1
	syscall
	b	L3
L1:
	lw	$t0,_j
	bgt	$t0,1200,L5
	b	L4
L5:
	lw	$t1,_j
	lw	$t0,_i
	add	$t0,$t0,$t1
	add	$t0,$t0,1
	move	$a0,$t0
	li	$v0,1
	syscall
	b	L6
L4:
	li	$a0,234
	li	$v0,1
	syscall
L6:
L3:
	la	$a0,_newline
	li	$v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
