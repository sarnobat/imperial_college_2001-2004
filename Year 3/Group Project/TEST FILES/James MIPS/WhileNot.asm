# compiled from WhileNot
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
	li	$t0,1230
	sw	$t0,_x
L1:
	lw	$t0,_x
 ge	$t0,1234,L2
	b	L3
L3:
	lw	$t0,_x
	add	$t0,$t0,2
	sw	$t0,_x
	b	L1
L2:
	lw	$a0,_x
	li	$v0,1
	syscall
	la	$a0,_newline
	li	$v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
