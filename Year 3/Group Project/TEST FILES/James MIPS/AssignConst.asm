# compiled from AssignConst
# statically allocated variables here:
	.data
_x:
	.space	4
_newli :
	.asciiz "\n"	# newline
# start of the generated code
	.text
	.globl	main
main:
	li	$t0,1234
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
