# Compiled from WhileLess
# Statically allocated variables here
	.data
_x:	.space	4
_newline:	 sciiz	"\n"
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
	lw $t1,_x
	li $t2,100
	add $t0,$t1,$t2
	sw $t0,_x
	b L0
L1:
L3:
	lw $t0,_x
	li $t1,1230
	blt $t0,$t1,L5
	b L4
L5:
	lw $t1,_x
	li $t2,10
	add $t0,$t1,$t2
	sw $t0,_x
	b L3
L4:
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
