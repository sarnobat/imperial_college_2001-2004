# Compiled from AllRegisters
# Statically allocated variables here
	.data
_n:	.space	4 m:	.space	4
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t1,50
	neg $t0,$t1
	sw $t0,_m
	li $t0,90
	sw $t0,_n
	li $t3,10
	li $t7,5
	li $t8,4
	mul $t6,$t7,$t8
	li $t7,1
	add $t5,$t6,$t7
	li $t8,1
	lw $s0,_m
	li $s2,10
	li $s4,20
	li $s6,30
