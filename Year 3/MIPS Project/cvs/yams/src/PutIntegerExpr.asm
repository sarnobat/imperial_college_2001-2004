# Compiled from PutIntegerExpr
# Statically allocated variables here
	.data
_x:	.space 
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t0,4
	sw $t0,_x
	li $t0,1230
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
