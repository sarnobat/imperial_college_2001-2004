# Compiled from EvalExprDivVar
# Statically allocated variables here
	.data
_z:	.space 
_y:	.space	4
_x:	.space	4
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t0,10
	sw $t0,_y
	li $t1,1234
	lw $t2,_y
	mul $t0,$t1,$t2
	sw $t0,_z
	lw $t1,_z
	li $t2,10
	div $t0,$t1,$t2
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
