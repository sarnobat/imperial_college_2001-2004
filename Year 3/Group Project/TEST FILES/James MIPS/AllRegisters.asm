# compiled from AllRegisters
# statically allocated variables here:
	.data
_n:
	.space	4
_m:
	.space	4
_newline:
	.asciiz "\n"	# newline
# start of the generated code
	.text
	.globl	main
main:
	li	$t0,50
	neg	$t0,$t0
	sw	$t0,_m
	li	$t0,90
	sw	$t0,_n
	lw	$t1,_n
	li	$t0,30
	sub	$t0,$t0,$t1
	neg	$t0,$t0
	li	$t1,100
	div	$t1,$t1,$t0
	lw	$t0,_n
	sub	$t0,$t0,$t1
	li	$t1,80
	sub	$t1,$t1,$t0
	li	$t0,70
	sub	$t0,$t0,$t1
	li	$t1,60
	sub	$t1,$t1,$t0
	li	$t0,50
	sub	$t0,$t0,$t1
	li	$t1,40
	sub	$t1,$t1,$t0
	li	$t0,30
	sub	$t0,$t0,$t1
	li	$t1,20
	sub	$t1,$t1,$t0
	li	$t0,10
	sub	$t0,$t0,$t1
	lw	$t1,_m
	sub	$t1,$t1,$t0
	li	$t0,1
	mul	$t0,$t0,$t1
	neg	$t0,$t0
	li	$t1,5
	mul	$t1,$t1,4
	add	$t1,$t1,1
	add	$t1,$t1,$t0
	li	$t0,10
	mul	$t0,$t0,$t1
	li	$t1,2
	mul	$t1,$t1,15
	add	$t0,$t0,$t1
	add	$t0,$t0,4
	sw	$t0,_m
	lw	$a0,_m
	li	$v0,1
	syscall
	la	$a0,_newline
	li	$v0,4
	syscall
# exit call to stop the program
	li	$v0,10
	syscall
