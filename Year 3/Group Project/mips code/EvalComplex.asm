# Compiled from EvalExprPlus
# Statically allocated variables here
	.data
_y:	.space	4
_x:	.sp  e	4
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t0,2
	sw $t0,_y
	lw $t3,_y
	lw $t5,_y
	li $t7,200
	li $s1,4
	lw $s2,_y
	mul $s0,$s1,$s2
	li $s1,5
	mul $t9,$s0,$s1
	li $s0,10
	mul $t8,$t9,$s0
	add $t6,$t7,$t8
	mul $t4,$t5,$t6
	add $t2,$t3,$t4
	li $t4,17
	lw $t5,_y
	mul $t3,$t4,$t5
	add $t1,$t2,$t3
	li $t2,2
	sub $t0,$t1,$t2
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
