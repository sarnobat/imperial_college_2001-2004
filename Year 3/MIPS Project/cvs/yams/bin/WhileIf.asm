# Compiled from WhileIf
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
	li t0,60
	sw $t0,_y
	lw $t0,_y
	li $t1,100
	blt $t0,$t1,L3
L3:
	lw $t1,_y
	li $t2,40
	add $t0,$t1,$t2
	sw $t0,_y
L4:
	lw $t1,_x
	lw $t2,_y
	add $t0,$t1,$t2
	sw $t0,_x
	b L0
L1:
L5:
	lw $t0,_x
	li $t1,1230
	blt $t0,$t1,L7
	b L6
L7:
	lw $t1,_x
	li $t2,10
	add $t0,$t1,$t2
	sw $t0,_x
	b L5
L6:
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
