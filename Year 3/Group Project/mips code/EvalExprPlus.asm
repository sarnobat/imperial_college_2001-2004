# Compiled from EvalExprPlus
# Statically allocated variables here
	.data
_x:	.space	4 newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t1,1200
	li $t2,34
	add $t0,$t1,$t2
	sw $t0,_x
	lw $a0,_x
	li $v0,1
	syscall
	la $a0,_newline
	li $v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
