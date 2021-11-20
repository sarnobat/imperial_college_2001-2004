# Compiled from RunOutOfRegisters
# Statically allocated variables here
	.data
_c:	.space	4
_a:	.space	4
_jj:	.space	4
_result:	.space	4
_bb:	.space	4
_z:	.space	4
_y:	.space	4
_x:	.space	4
_w:	.space	4
_v:	.space	4
_u:	.space	4
_t:	.space	4
_s:	.space	4
_r:	.space	4
_q:	.space	4
_p:	.space	4
_o:	.space	4
_n:	.space	4
_m:	.space	4
_l:	.space	4
_k:	.space	4
_i:	.space	4
_h:	.space	4
_g:	.space	4
_f:	.space	4
_e:	.space	4
_d:	.space	4
_newline:	.asciiz	"\n"
	.text
	.globl main
main:
	li $t0,200
	sw $t0,_a
	li $t0,0
	sw $t0,_bb
	li $t0,0
	sw $t0,_c
	li $t0,5
	sw $t0,_d
	li $t0,0
	sw $t0,_e
	li $t0,200
	sw $t0,_f
	li $t0,5
	sw $t0,_g
	li $t0,0
	sw $t0,_h
	li $t0,0
	sw $t0,_i
	li $t0,200
	sw $t0,_jj
	li $t0,5
	sw $t0,_k
	li $t0,0
	sw $t0,_l
	li $t0,0
	sw $t0,_m
	li $t0,200
	sw $t0,_n
	li $t0,5
	sw $t0,_o
	li $t0,0
	sw $t0,_p
	li $t0,0
	sw $t0,_q
	li $t0,200
	sw $t0,_r
	li $t0,5
	sw $t0,_s
	li $t0,0
	sw $t0,_t
	li $t0,0
	sw $t0,_u
	li $t0,200
	sw $t0,_v
	li $t0,5
	sw $t0,_w
	li $t0,0
	sw $t0,_x
	li $t0,2
	sw $t0,_y
	li $t0,6
	sw $t0,_z
	lw $t8,_a
	lw $t9,_bb
	sub $t7,$t8,$t9
	lw $t9,_c
	lw $s0,_d
	sub $t8,$t9,$s0
	sub $t6,$t7,$t8
	lw $t9,_e
	lw $s0,_f
	sub $t8,$t9,$s0
	lw $s0,_g
	lw $s1,_h
	sub $t9,$s0,$s1
	sub $t7,$t8,$t9
	sub $t5,$t6,$t7
	lw $t8,_i
	lw $t9,_jj
	sub $t7,$t8,$t9
	lw $t9,_k
	lw $s0,_l
	sub $t8,$t9,$s0
	sub $t6,$t7,$t8
	sub $t4,$t5,$t6
	lw $t7,_m
	lw $t8,_n
	sub $t6,$t7,$t8
	lw $t8,_o
	lw $t9,_p
	sub $t7,$t8,$t9
	sub $t5,$t6,$t7
	sub $t3,$t4,$t5
	lw $t6,_q
	lw $t7,_r
	sub $t5,$t6,$t7
	lw $t7,_s
	lw $t8,_t
	sub $t6,$t7,$t8
	sub $t4,$t5,$t6
	sub $t2,$t3,$t4
	lw $t5,_u
	lw $t6,_v
	sub $t4,$t5,$t6
	lw $t6,_w
	lw $t7,_x
	sub $t5,$t6,$t7
	sub $t3,$t4,$t5
	sub $t1,$t2,$t3
	lw $t3,_y
	lw $t4,_z
	sub $t2,$t3,$t4
	sub $t0,$t1,$t2
	sw $t0,_result
	lw $a0,_result
	li $v0,1
	syscall
	la $a0,_newline
	li $v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
