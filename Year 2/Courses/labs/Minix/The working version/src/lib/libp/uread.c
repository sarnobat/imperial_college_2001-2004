/* $Header: uread.c,v 2.3 91/02/22 16:57:33 ceriel Exp $ */
/*
 * (c) copyright 1983 by the Vrije Universiteit, Amsterdam, The Netherlands.
 *
 *          This product is part of the Amsterdam Compiler Kit.
 *
 * Permission to use sell, duplicate or disclose this software must be
 * obtained in writing. Requests for such permissions may be sent to
 *
 *      Dr. Andrew S. Tanenbaum
 *      Wiskundig Seminarium
 *      Vrije Universiteit
 *      Postbox 7161
 *      1007 MC Amsterdam
 *      The Netherlands
 *
 */

/* function uread(fd:integer; var b:buf; n:integer):integer; */

extern int	_read();

int uread(fd,b,n) char *b; int fd,n; {
	return(_read(fd,b,n));
}
