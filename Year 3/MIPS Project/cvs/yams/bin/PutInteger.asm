# Compiled from PutInteger
# Statically allocated variables here
	.data
_newline:	.asciiz	"\n" .text
	.globl main
main:
	li $a0,1234
	li $v0,1
	syscall
	la $a0,_newline
	li $v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
