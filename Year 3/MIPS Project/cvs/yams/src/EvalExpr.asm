# Compiled from EvalExpr
# Statically allocated variables here
	.data
_x:	.space	4
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t0,10
	sw $t0,_x
	li $t6,1
	lw $t7,_x
	mul $t5,$t6,$t7
	li $t6,2
	add $t4,$t5 t6
	lw $t5,_x
	mul $t3,$t4,$t5
	li $t4,3
	add $t2,$t3,$t4
	lw $t3,_x
	mul $t1,$t2,$t3
	li $t2,4
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
