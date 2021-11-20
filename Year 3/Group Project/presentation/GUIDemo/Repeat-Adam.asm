# Compiled from Repeat
# Statically allocated variables here
	.data
_y:	.space	4
_x:	.space	4
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t0,4
	sw $t0,_x
	li $t0,0
	sw $t0,_y
L0:
	lw $t1,_x
	li $t2,100
	add $t0,$t1,$t2
	sw $t0,_x
	lw $t1,_y
	li $t2,1
	add $t0,$t1,$t2
	sw $t0,_y
	lw $t0,_y
	li $t1,12
	beq $t0,$t1,L1
	b L0
L1:
	li $t0,0
	sw $t0,_y
L2:
	lw $t1,_x
	li $t2,10
	add $t0,$t1,$t2
	sw $t0,_x
	lw $t1,_y
	li $t2,1
	add $t0,$t1,$t2
	sw $t0,_y
	lw $t0,_y
	li $t1,3
	beq $t0,$t1,L3
	b L2
L3:
	lw $a0,_x
	li $v0,1
	syscall
	la $a0,_newline
	li $v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
