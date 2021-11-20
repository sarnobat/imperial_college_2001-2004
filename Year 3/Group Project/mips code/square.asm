# Compiled from square
# Statically allocated variables here
	.data
_j:	.space	4
_i:	.space	4
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t0,1
	sw $t0,_i
L0:
	lw $t0,_i
	li $t1,11
	ble $t0,$t1,L2
	b L1
L2:
	li $t 1
	sw $t0,_j
L3:
	lw $t0,_j
	li $t1,11
	blt $t0,$t1,L5
	b L4
L5:
	lw $t0,_j
	lw $t1,_i
	blt $t0,$t1,L6
	b L7
L6:
	li $a0,0
	li $v0,1
	syscall
	b L8
L7:
	li $a0,1
	li $v0,1
	syscall
L8:
	lw $t1,_j
	li $t2,1
	add $t0,$t1,$t2
	sw $t0,_j
	b L3
L4:
	lw $t1,_i
	li $t2,1
	add $t0,$t1,$t2
	sw $t0,_i
	la $a0,_newline
	li $v0,4
	syscall
	b L0
L1:
# exit call to stop the program
	li	$v0,10
	syscall
