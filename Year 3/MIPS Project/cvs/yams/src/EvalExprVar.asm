# Compiled from EvalExprVar
# Statically allocated variables here
	.data
_y:	.space	4
_x:	.spa 	4
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t0,12
	sw $t0,_x
	lw $t2,_x
	li $t3,100
	mul $t1,$t2,$t3
	li $t2,34
	add $t0,$t1,$t2
	sw $t0,_y
	lw $a0,_y
	li $v0,1
	syscall
	la $a0,_newline
	li $v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
