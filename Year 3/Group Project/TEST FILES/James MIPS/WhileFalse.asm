# compiled from WhileFalse
# statically allocated variables here:
	.data
_newline:
	.asciiz "\ 	# newline
# start of the generated code
	.text
	.globl	main
main:
L1:
	li	$t0,1
	bne	$t0,1,L3
	b	L2
L3:
	li	$a0,456
	li	$v0,1
	syscall
	b	L1
L2:
	li	$a0,1234
	li	$v0,1
	syscall
	la	$a0,_newline
	li	$v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
