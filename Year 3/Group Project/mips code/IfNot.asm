# Compiled from IfNot
# Statically allocated variables here
	.data
_x:	.space	4
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t0,20
	sw $t0,_x
	lw $t0,_x
	li $t1,20
	bgt $t0,$t1,L3
	b L0
L3:
	b L1
L0:
	li $a0,1234
 i $v0,1
	syscall
	b L2
L1:
	lw $a0,_x
	li $v0,1
	syscall
L2:
	la $a0,_newline
	li $v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
