# Compiled from WhileNot
# Statically allocated variables here
	.data
_x:	.space	4
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t0,1230
	sw $t0,_x
L0:
	lw $t0,_x
	li $t1,1234
	bge $t0,$t1,L3
	b L2
L3:
	b L� L2:
	lw $t1,_x
	li $t2,2
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
