#Code generated by the lab compiler version 2.1
#Module name NestedOrFalse
	.data
_x:	 pace	4
__newline:
	.asciiz	"\n"
	.text
	.globl main
main:	#first instruction
	li	$a1,5
	sw	$a1,_x
	bgt	$a1,10,L1
	b	L4
L4:
	blt	$a1,0,L1
	b	L3
L3:
	li	$a3,1
	div	$a2,$a3,$a1
	bgt	$a2,0,L1
	b	L7
L7:
	lw	$a1,_x
	beq	$a1,10,L1
	b	L2
L1:
	li	$a0,456
	li	$v0,1
	syscall
	b	L10
L2:
	li	$a0,1234
	li	$v0,1
	syscall
L10:
	la	$a0,__newline
	li	$v0,4
	syscall
#sycall exit to end program
	li	$v0,10
	syscall
