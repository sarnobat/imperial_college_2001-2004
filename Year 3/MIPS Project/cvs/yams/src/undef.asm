# Compiled from undef
# Statically allocated variables here
	.data
_y:	.space	4
_x:	.space	4
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	lw $t0,_y
	li $t1,0
	bgt $t0,$t1,L0
	b L1
L0:
	li $t0,4
	sw $t0,_x
L1:
	li $t0, 34
	lw $t1,_x
	add $a0,$t0,$t1
	li $v0,1
	syscall
	la $a0,_newline
	li $v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
