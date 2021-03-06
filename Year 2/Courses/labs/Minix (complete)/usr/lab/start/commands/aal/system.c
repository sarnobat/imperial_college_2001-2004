/*
 * (c) copyright 1987 by the Vrije Universiteit, Amsterdam, The Netherlands.
 * See the copyright notice in the ACK home directory, in the file "Copyright".
 */
/* RCS: $Header: system.c,v 1.2 87/03/09 15:05:46 ceriel E  $ */

#include <system.h>

File _sys_ftab[SYS_NOPEN] = {
	{ 0, OP_READ},
	{ 1, OP_APPEND},
	{ 2, OP_APPEND}
};

File *
_get_entry()
{
	register File *fp;

	for (fp = &_sys_ftab[0]; fp < &_sys_ftab[SYS_NOPEN]; fp++)
		if (fp->o_flags == 0)
			return fp;
	return (File *)0;
}
