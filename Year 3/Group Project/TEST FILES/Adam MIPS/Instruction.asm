# Compiled from Instruction
# Statically allocated variables here
	.data
_b:	.space	4
_j:	.spa 	4
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t0,11105
	sw $t0,_b
	li $t0,1
	sw $t0,_j
	lw $t1,_b
	lw $t2,_j
	add $t0,$t1,$t2
	sw $t0,_b
	lw $t0,_b
	li $t1,9
	div $a0,$t0,$t1
	li $v0,1
	syscall
	la $a0,_newline
	li $v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
