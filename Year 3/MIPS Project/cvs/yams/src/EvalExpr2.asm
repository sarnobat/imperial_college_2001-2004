# Compiled from EvalExpr2
# Statically allocated variables here
	.data
_y:	.space	4
_x:	.space 
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t0,2
	sw $t0,_y
	li $t0,10
	sw $t0,_x
	lw $t2,_y
	lw $t4,_y
	li $t6,200
	li $s0,4
	lw $s1,_y
	mul $t9,$s0,$s1
	li $s0,5
	mul $t8,$t9,$s0
	lw $t9,_x
	mul $t7,$t8,$t9
	add $t5,$t6,$t7
	mul $t3,$t4,$t5
	add $t1,$t2,$t3
	li $t3,17
	lw $t4,_y
	mul $t2,$t3,$t4
	add $t0,$t1,$t2
	li $t1,2
	sub $a0,$t0,$t1
	li $v0,1
	syscall
	la $a0,_newline
	li $v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
