/*
 * (c) copyright 1987 by the Vrije Universiteit, Amsterdam, The Netherlands.
 * See the copyright notice in the ACK home directory, in the file "Copyright".
 */
/* $Header: write.c,v 1.3 89/06/30 14:46:27 ceriel Exp $ */

#incl e <system.h>

int
sys_write(fp, bufptr, nbytes)
	File *fp;
	char *bufptr;
	int nbytes;
{
	if (! fp) return 0;
	return write(fp->o_fd, bufptr, nbytes) == nbytes;
}
