#Code generated by the lab compiler version 2.1
#Module name CompareExpression 	.data
_z:	.space	4
_y:	.space	4
_x:	.space	4
__newline:
	.asciiz	"\n"
	.text
	.globl main
main:	#first instruction
	li	$a1,2
	sw	$a1,_x
	li	$a2,3
	sw	$a2,_y
	li	$a3,4
	sw	$a3,_z
	add	$v1,$a1,$a3
	add	$a1,$a2,$a3
	blt	$v1,$a1,L1
	b	L2
L1:
	add	$a0,$a3,1230
	li	$v0,1
	syscall
	b	L4
L2:
	lw	$a2,_x
	add	$a0,$a2,1230
	li	$v0,1
	syscall
L4:
#sycall exit to end program
	li	$v0,10
	syscall
