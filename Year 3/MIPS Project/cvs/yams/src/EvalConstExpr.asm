# Compiled from EvalConstExpr
# Statically allocated variables here
	.data
_x:	.space	 _newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t6,1
	li $t7,10
	mul $t5,$t6,$t7
	li $t6,2
	add $t4,$t5,$t6
	li $t6,1
	li $t7,9
	add $t5,$t6,$t7
	mul $t3,$t4,$t5
	li $t4,3
	add $t2,$t3,$t4
	li $t4,2
	li $t5,8
	add $t3,$t4,$t5
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
