# compiled from BigConst
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
	li	$t0,1234000
	sw	$t0,_ 	li	$t0,123
	mul	$t0,$t0,1000
	add	$t0,$t0,4000
	sw	$t0,_y
	lw	$t0,_x
	beq	$t0,1234000,L2
	b	L1
L2:
	lw	$t0,_x
	div	$t0,$t0,1000
	move	$a0,$t0
	li	$v0,1
	syscall
	b	L3
L1:
	lw	$t1,_y
	lw	$t0,_x
	div	$t0,$t0,$t1
	move	$a0,$t0
	li	$v0,1
	syscall
L3:
	la	$a0,_newline
	li	$v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
