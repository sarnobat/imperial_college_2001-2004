# Compiled from IfOr
# Statically allocated variables here
	.data
_i:	.space	4
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t0,43
	sw $t0,_i
	lw $t0,_i
	li $t1,41
	blt $t0,$t1,L0
	lw $t0,_i
	li $t1,98
	bne $t0,$t1 0
	b L1
L0:
	li $a0,1234
	li $v0,1
	syscall
	b L2
L1:
	li $a0,456
	li $v0,1
	syscall
L2:
	la $a0,_newline
	li $v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
