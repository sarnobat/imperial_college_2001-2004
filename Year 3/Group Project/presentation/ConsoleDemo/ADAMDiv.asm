# Compiled from EvalExprDiv
# Statically allocated variables here
	.data
_x:	.space	4
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t1,12340
	li $t2,10
	div $t0,$t1,$t2
	sw $t0,_x
	lw $a0,_x
	li $v0,1
	syscall
	la  0,_newline
	li $v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
