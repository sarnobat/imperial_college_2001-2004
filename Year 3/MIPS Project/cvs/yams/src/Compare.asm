# Compiled from Compare
# Statically allocated variables here
	.data
_x:	.space	4
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t0,0
	sw $t0,_x
	lw $t0,_x
	li $t1,0
	beq $t0,$t1,L0
	b L1
L0:
	li $t0,2
	sw $t0,_x
	b L2
L1:
	li $t0,1
	sw $t0,_x
L2:
	lw $t0,_x
	li $t1,0
	bgt $t0,$t1,L3
	b L4
L3:
	li $t0,34
	sw $t0,_x
	b L5
L4:
	li $t0,2
	sw $t0,_x
L5:
	lw $t0,_x
	li $t1,40
	blt $t0,$t1,L6
	b L7
L6:
	li $t1,200
	lw $t2,_x
	add $t0,$t1,$t2
	sw $t0,_x
	b L8
L7:
	li $t0,3
	sw $t0,_x
L8:
	lw $t0,_x
	li $t1,200
	bge $t0,$t1,L9
	b L10
L9:
	lw $t1,_x
	li $t2,1000
	add $t0,$t1,$t2
	sw $t0,_x
	b L11
L10:
	li $t0,4
	sw $t0,_x
L11:
	lw $t0,_x
	li $t1,0
	bne $t0,$t1,L12
	b L13
L12:
	lw $t1,_x
	li $t2,1
	add $t0,$t1,$t2
	sw $t0,_x
	b L14
L13:
	li $t0,5
	sw $t0,_x
L14:
	lw $t0,_x
	li $t1,2000
	ble $t0,$t1,L15
	b L16
L15:
	lw $t1,_x
	li $t2,1
	sub $t0,$t1,$t2
	sw $t0,_x
	b L17
L16:
	li $t0,6
	sw $t0,_x
L17:
	lw $a0,_x
	li $v0,1
	syscall
	la $a0,_newline
	li $v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
