# Compiled from IfAndNotVar
# Statically allocated variables here
	.data
_j:	.space	4
_i:	.spa 	4
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t0,43
	sw $t0,_i
	li $t0,44
	sw $t0,_j
	lw $t0,_i
	lw $t1,_j
	bge $t0,$t1,L5
	b L3
L5:
	b L4
L3:
	lw $t0,_i
	lw $t2,_j
	li $t3,1
	add $t1,$t2,$t3
	beq $t0,$t1,L6
	b L0
L6:
L4:
	b L1
L0:
	li $a0,1234
	li $v0,1
	syscall
	b L2
L1:
	li $a0,456
	li $v0,1
	syscall
L2:
	la $a0,_newline
	li $v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
