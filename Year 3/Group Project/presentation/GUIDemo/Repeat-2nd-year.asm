#Code generated by the lab compiler version 2.1
#Module name Repeat
	.data
_y:	.space	 _x:	.space	4
__newline:
	.asciiz	"\n"
	.text
	.globl main
main:	#first instruction
	li	$a1,4
	sw	$a1,_x
	sw	$zero,_y
L1:
	lw	$a2,_x
	add	$a1,$a2,100
	sw	$a1,_x
	lw	$a3,_y
	add	$a2,$a3,1
	sw	$a2,_y
	beq	$a2,12,L2
	b	L1
L2:
	sw	$zero,_y
L4:
	lw	$a2,_x
	add	$a1,$a2,10
	sw	$a1,_x
	lw	$a3,_y
	add	$a2,$a3,1
	sw	$a2,_y
	beq	$a2,3,L5
	b	L4
L5:
	lw	$a0,_x
	li	$v0,1
	syscall
	la	$a0,__newline
	li	$v0,4
	syscall
#sycall exit to end program
	li	$v0,10
	syscall
