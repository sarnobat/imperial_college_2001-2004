# Compiled from IfFalseElseVar
# Statically allocated variables here
	.data
_y:	.space 
_x:	.space	4
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t0,2
	sw $t0,_x
	li $t0,1
	sw $t0,_y
	lw $t1,_x
	lw $t2,_y
	sub $t0,$t1,$t2
	li $t1,1
	bne $t0,$t1,L0
	b L1
L0:
	li $a0,456
	li $v0,1
	syscall
	b L2
L1:
	li $a0,1234
	li $v0,1
	syscall
L2:
	la $a0,_newline
	li $v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
