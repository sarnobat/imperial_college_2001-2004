# Compiled from IfNested
# Statically allocated variables here
	.data
_j:	.space	4
_i:	.space	4
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t0,3
	sw $t0,_i
	li $t0,1230
	sw $t0,_j
	lw $t0,_i
	li $t1,2
	bl $t0,$t1,L0
	b L1
L0:
	li $a0,456
	li $v0,1
	syscall
	b L2
L1:
	lw $t0,_j
	li $t1,1200
	bgt $t0,$t1,L3
	b L4
L3:
	lw $t1,_i
	lw $t2,_j
	add $t0,$t1,$t2
	li $t1,1
	add $a0,$t0,$t1
	li $v0,1
	syscall
	b L5
L4:
	li $a0,234
	li $v0,1
	syscall
L5:
L2:
	la $a0,_newline
	li $v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
