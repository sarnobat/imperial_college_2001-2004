# Compiled from WhileJumpAnd
# Statically allocated variables here
	.data
_x:	.space	4 newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t0,0
	sw $t0,_x
L0:
	lw $t0,_x
	li $t1,0
	bgt $t0,$t1,L3
	b L4
L3:
	li $t1,1
	lw $t2,_x
	div $t0,$t1,$t2
	li $t1,0
	bgt $t0,$t1,L2
L4:
	b L1
L2:
	li $a0,456
	li $v0,1
	syscall
	b L0
L1:
	li $a0,1234
	li $v0,1
	syscall
	la $a0,_newline
	li $v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
