# compiled from IfList
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
	li	$t0,123
	sw	$t0,_x
	lw	$t0,_x
	 t	$t0,$zero,L2
	b	L1
L2:
	lw	$t0,_x
	mul	$t0,$t0,10
	sw	$t0,_x
	lw	$t0,_x
	add	$t0,$t0,4
	sw	$t0,_x
L1:
	lw	$a0,_x
	li	$v0,1
	syscall
	la	$a0,_newline
	li	$v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
