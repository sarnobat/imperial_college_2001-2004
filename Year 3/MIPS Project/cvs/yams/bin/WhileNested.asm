# Compiled from WhileNested
# Statically allocated variables here
	.data
_y:	.space	4
_x:	.space	4
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t0,0
	sw $t0,_x
L0:
	lw $t0,_x
	li $t1,1200
	blt $t0,$t1,L2
	b L1
L2:
	li $t0,60
	sw $t0,_y
L3:
	lw $t0,_y
	li $t1,100
	blt $t0,$t1,L5
	b L4
L5:
	lw $t1,_y
	li $t2,20
	add $t0,$t1,$t2
	sw $t0,_y
	b L3
L4:
	lw $t1,_x
	lw $t2,_y
	add $t0,$t1,$t2
	sw $t0,_x
	b L0
L1:
L6:
	lw $t0,_x
	li $t1,1230
	blt $t0,$t1,L8
	b L7
L8:
	lw $t1,_x
	li $t2,10
	add $t0,$t1,$t2
	sw $t0,_x
	b L6
L7:
	lw $t1,_x
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
