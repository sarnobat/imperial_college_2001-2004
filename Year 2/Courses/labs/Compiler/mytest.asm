#Code generated by the Sridhar Sarnobat's compiler
#Module name mytest
	.data
_k:	.space	4
_j:	.space	4
_i:	.space	4
__newline:
	.asciiz "\n"
	.text
	.globl	main
main:
	li	$t0,1
	sw	$t0,_i
	li	$t0,2
	sw	$t0,_j
	li	$t0,3
	sw	$t0,_k
	li	$t0,1
	sw	$t0,_i
	li	$t0,2
	sw	$t0,_j
	li	$t0,3
	sw	$t0,_k
	li	$t0,1
	sw	$t0,_i
	li	$t0,2
	sw	$t0,_j
	li	$t0,3
	sw	$t0,_k
	li	$t0,1
	sw	$t0,_i
	li	$t0,2
	sw	$t0,_j
	li	$t0,3
	sw	$t0,_k
	li	$t0,1
	sw	$t0,_i
	li	$t0,2
	sw	$t0,_j
	li	$t0,3
	sw	$t0,_k
	li	$t0,1
	sw	$t0,_i
	li	$t0,2
	sw	$t0,_j
	li	$t0,3
	sw	$t0,_k
	li	$t0,1
	sw	$t0,_i
	li	$t0,2
	sw	$t0,_j
	li	$t0,3
	sw	$t0,_k
	li	$t0,1
	sw	$t0,_i
	li	$t0,2
	sw	$t0,_j
	li	$t0,3
	sw	$t0,_k
	li	$t0,1
	sw	$t0,_i
	li	$t0,2
	sw	$t0,_j
	li	$t0,3
	sw	$t0,_k
	li	$t0,1
	sw	$t0,_i
	li	$t0,2
	sw	$t0,_j
	li	$t0,3
	sw	$t0,_k
	li	$t0,1
	sw	$t0,_i
	li	$t0,2
	sw	$t0,_j
	li	$t0,3
	sw	$t0,_k
	li	$t0,1
	sw	$t0,_i
	li	$t0,2
	sw	$t0,_j
	li	$t0,3
	sw	$t0,_k
	li	$t0,1
	sw	$t0,_i
	li	$t0,2
	sw	$t0,_j
	li	$t0,3
	sw	$t0,_k
	li	$t0,1
	sw	$t0,_i
	li	$t0,2
	sw	$t0,_j
	li	$t0,3
	sw	$t0,_k
	lw	$t0,_i
	lw	$t1,_j
	blt	$t0,$t1,L6
	b	L4
L6:
	lw	$t0,_k
	lw	$t1,_i
	bgt	$t0,$t1,L4
	b	L5
L4:
	lw	$t0,_j
	lw	$t1,_i
	blt	$t0,$t1,L9
	b	L7
L9:
	lw	$t0,_i
	lw	$t1,_i
	blt	$t0,$t1,L7
	b	L8
L7:
	lw	$t0,_k
	lw	$t1,_i
	bgt	$t0,$t1,L2
	b	L3
L8:
	b	L3
L5:
	b	L3
L2:
	lw	$t0,_i
	lw	$t1,_j
	blt	$t0,$t1,L1
	b	L0
L3:
	b	L0
L0:
	li	$t0,1111
	move	$a0,$t0
	li	$v0,1
	syscall
	b	L10
L1:
	li	$t0,2222
	move	$a0,$t0
	li	$v0,1
	syscall
L10:
#sycall exit to end program
	li	$v0,10
	syscall