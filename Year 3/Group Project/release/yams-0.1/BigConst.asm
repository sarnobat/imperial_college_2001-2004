# Compiled from BigConst
# Statically allocated variables here
	.data
_y:	.space	4
_x:	.space	4
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t0,1234000
	sw $t0,_x
	li $t2,123
	li $t3,1000
	mul $t1,$t2,$t3
 i $t2,4000
	add $t0,$t1,$t2
	sw $t0,_y
	lw $t0,_x
	li $t1,1234000
	beq $t0,$t1,L0
	b L1
L0:
	lw $t0,_x
	li $t1,1000
	div $a0,$t0,$t1
	li $v0,1
	syscall
	b L2
L1:
	lw $t0,_x
	lw $t1,_y
	div $a0,$t0,$t1
	li $v0,1
	syscall
L2:
	la $a0,_newline
	li $v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
