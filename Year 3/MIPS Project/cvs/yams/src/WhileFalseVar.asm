# Compiled from WhileFalseVar
# Statically allocated variables here
	.data
_x:	.space	 _newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t0,1
	sw $t0,_x
L0:
	li $t0,1
	lw $t1,_x
	bne $t0,$t1,L2
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
