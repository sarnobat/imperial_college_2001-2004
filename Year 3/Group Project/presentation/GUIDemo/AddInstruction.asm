# Compiled from AssignConst
# Statically allocated variables here
	.data
_write:			.as> iz	"2 x 10 = "
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	la $a0,_write
	li $v0,4
	syscall
	muli $a0,2,10
	li $v0,1
	syscall
	la $a0,_newline
	li $v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall