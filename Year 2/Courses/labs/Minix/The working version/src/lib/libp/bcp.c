/* $Header: bcp.c,v 2.2 87/05/19 08:51:48 ceriel Exp $ */
/*
 * (c) copyright 1983 by the Vrije Universiteit, Amsterdam, The Netherlands.
 *
 *          This product is part of the Amsterdam Compiler Kit.
 *
 * Permission to use,  ll, duplicate or disclose this software must be
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

/* Author: J.W. Stevenson */

int _bcp(sz,y,x) int sz; unsigned char *y,*x; {

	while (--sz >= 0) {
		if (*x < *y)
			return(-1);
		if (*x++ > *y++)
			return(1);
	}
	return(0);
}
