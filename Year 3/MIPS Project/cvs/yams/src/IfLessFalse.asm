# Compiled from IfLessFalse
# Statically allocated variables here
	.data
_newline:	.asciiz	"\n 	.text
	.globl main
main:
	li $t0,2
	li $t1,3
	blt $t0,$t1,L0
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
