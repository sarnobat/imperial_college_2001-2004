# compiled from RunOutOfRegisters
# statically allocated variables here:
	.data
_c:
	.space	4
_a:
	.space	4
_jj:
	.space	4
_result:
	.space	4
_bb:
	.space	4
_z:
	.space	4
_y:
	.space	4
_x:
	.space	4
_w:
	.space	4
_v:
	.space	4
_u:
	.space	4
_t:
	.space	4
_s:
	.space	4
_r:
	.space	4
_q:
	.space	4
_p:
	.space	4
_o:
	.space	4
_n:
	.space	4
_m:
	.space	4
_l:
	.space	4
_k:
	.space	4
_i:
	.space	4
_h:
	.space	4
_g:
	.space	4
_f:
	.space	4
_e:
	.space	4
_d:
	.space	4
_newline:
	.asciiz "\n"	# newline
# start of the generated code
	.text
	.globl	main
main:
	li	$t0,200
	sw	$t0,_a
	sw	$zero,_bb
	sw	$zero,_c
	li	$t0,5
	sw	$t0,_d
	sw	$zero,_e
	li	$t0,200
	sw	$t0,_f
	li	$t0,5
	sw	$t0,_g
	sw	$zero,_h
	sw	$zero,_i
	li	$t0,200
	sw	$t0,_jj
	li	$t0,5
	sw	$t0,_k
	sw	$zero,_l
	sw	$zero,_m
	li	$t0,200
	sw	$t0,_n
	li	$t0,5
	sw	$t0,_o
	sw	$zero,_p
	sw	$zero,_q
	li	$t0,200
	sw	$t0,_r
	li	$t0,5
	sw	$t0,_s
	sw	$zero,_t
	sw	$zero,_u
	li	$t0,200
	sw	$t0,_v
	li	$t0,5
	sw	$t0,_w
	sw	$zero,_x
	li	$t0,2
	sw	$t0,_y
	li	$t0,6
	sw	$t0,_z
	lw	$t1,_h
	lw	$t0,_g
	sub	$t0,$t0,$t1
	lw	$t2,_f
	lw	$t1,_e
	sub	$t1,$t1,$t2
	sub	$t1,$t1,$t0
	lw	$t0,_d
	lw	$t2,_c
	sub	$t2,$t2,$t0
	lw	$t3,_bb
	lw	$t0,_a
	sub	$t0,$t0,$t3
	sub	$t0,$t0,$t2
	sub	$t0,$t0,$t1
	lw	$t1,_l
	lw	$t2,_k
	sub	$t2,$t2,$t1
	lw	$t3,_jj
	lw	$t1,_i
	sub	$t1,$t1,$t3
	sub	$t1,$t1,$t2
	sub	$t0,$t0,$t1
	lw	$t1,_p
	lw	$t2,_o
	sub	$t2,$t2,$t1
	lw	$t3,_n
	lw	$t1,_m
	sub	$t1,$t1,$t3
	sub	$t1,$t1,$t2
	sub	$t0,$t0,$t1
	lw	$t1,_t
	lw	$t2,_s
	sub	$t2,$t2,$t1
	lw	$t3,_r
	lw	$t1,_q
	sub	$t1,$t1,$t3
	sub	$t1,$t1,$t2
	sub	$t0,$t0,$t1
	lw	$t1,_x
	lw	$t2,_w
	sub	$t2,$t2,$t1
	lw	$t3,_v
	lw	$t1,_u
	sub	$t1,$t1,$t3
	sub	$t1,$t1,$t2
	sub	$t0,$t0,$t1
	lw	$t2,_z
	lw	$t1,_y
	sub	$t1,$t1,$t2
	sub	$t0,$t0,$t1
	sw	$t0,_result
	lw	$a0,_result
	li	$v0,1
	syscall
	la	$a0,_newline
	li	$v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
