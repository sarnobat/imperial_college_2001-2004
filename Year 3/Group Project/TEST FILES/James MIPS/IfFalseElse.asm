# compiled from IfFalseElse
# statically allocated variables here:
	.data
_newline:
	.asciiz " "	# newline
# start of the generated code
	.text
	.globl	main
main:
	li	$t0,1
	bne	$t0,1,L2
	b	L1
L2:
	li	$a0,456
	li	$v0,1
	syscall
	b	L3
L1:
	li	$a0,1234
	li	$v0,1
	syscall
L3:
	la	$a0,_newline
	li	$v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
