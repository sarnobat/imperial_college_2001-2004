/* $Header: local.h,v 2.7 88/07/08 10:33:53 ceriel Exp $ */
/*
 * (c) copyright 1987 by the Vrije Universiteit, Amsterdam, The Netherlands.
 * See the copyright notice in the ACK home directory, in the file "Copyright".
 */
/* col ction of options, selected by including or excluding 'defines' */

/* Version number of the EM object code */
#	define	VERSION		3	/* 16 bits number */

/* The default machine used by ack, acc, apc */
#	define	ACKM		"minix"

/* size of local machine, either 0 (for 16 bit address space), or 1 */
#	undef BIGMACHINE	1

/* operating system, SYS_5, V7, BSD4_1 or BSD4_2; Do NOT delete the comment
   in the next line! */
#	define V7  1       /* SYSTEM */
