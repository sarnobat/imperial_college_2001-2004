# Compiled from WhileList
# Statically allocated variables here
	.data
_y:	.space	4
_x:	.space 
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t0,123
	sw $t0,_x
L0:
	lw $t0,_x
	li $t1,1000
	blt $t0,$t1,L2
	b L1
L2:
	lw $t1,_x
	li $t2,10
	mul $t0,$t1,$t2
	sw $t0,_x
	lw $t1,_x
	li $t2,4
	add $t0,$t1,$t2
	sw $t0,_x
	b L0
L1:
	lw $a0,_x
	li $v0,1
	syscall
	la $a0,_newline
	li $v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
