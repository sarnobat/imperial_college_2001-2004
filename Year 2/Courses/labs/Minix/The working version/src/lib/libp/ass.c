/* $Header: ass.c,v 2.1 84/07/20 11:02:58 sater Stab $ */
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

#include	<em_abs.h>
#include	<pc_err.h>

extern char	*_hol0();
extern		_trp();

_ass(line,bool) int line,bool; {

	if (bool==0) {
		LINO = line;
		_trp(EASS);
	}
}
