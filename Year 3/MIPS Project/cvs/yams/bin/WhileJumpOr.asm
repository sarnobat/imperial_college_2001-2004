# Compiled from WhileJumpOr
# Statically allocated variables here
	.data
_x:	.space	4
_newline .asciiz	"\n"
	.text
	.globl main
main:
	li $t0,0
	sw $t0,_x
L0:
	lw $t0,_x
	li $t1,0
	beq $t0,$t1,L2
	li $t1,1
	lw $t2,_x
	div $t0,$t1,$t2
	li $t1,0
	beq $t0,$t1,L2
	b L1
L2:
	li $a0,1234
	li $v0,1
	syscall
	li $t0,1
	sw $t0,_x
	b L0
L1:
	la $a0,_newline
	li $v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
