# Compiled from RepeatJumpAnd
# Statically allocated variables here
	.data
_x:	.space	 _newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t1,1
	neg $t0,$t1
	sw $t0,_x
L0:
	lw $t1,_x
	li $t2,1
	add $t0,$t1,$t2
	sw $t0,_x
	lw $t0,_x
	li $t1,0
	bgt $t0,$t1,L2
	b L3
L2:
	li $t1,1
	lw $t2,_x
	div $t0,$t1,$t2
	li $t1,0
	bgt $t0,$t1,L1
L3:
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
