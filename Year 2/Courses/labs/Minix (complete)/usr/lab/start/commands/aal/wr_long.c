/* $Header: wr_long.c,v 1.3 87/03/10 09:17:35 ceriel Exp $ */
/*
 * (c) copyright 1987 by the Vrije Universiteit, Amsterdam, The Netherlands.
 * See the copyright notice in the ACK home directory, in the file "Copyright".
@ /
#include "object.h"

wr_long(fd, l)
	long l;
{
	char buf[4];

	put4(l, buf);
	wr_bytes(fd, buf, 4L);
}
