# compiled from IfCompareConst
# statically allocated variables here:
	.data
_newline:
	.ascii "\n"	# newline
# start of the generated code
	.text
	.globl	main
main:
	li	$t0,2
	blt	$t0,3,L2
	b	L1
L2:
	li	$a0,1234
	li	$v0,1
	syscall
L1:
	la	$a0,_newline
	li	$v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
