/* $Header: rdc.c,v 2.2 84/07/20 11:19:31 sater Stab $ */
/*
 * (c) copyright 1983 by the Vrije Universiteit, Amsterdam, The Netherlands.
 *
 *          This product is part of the Amsterdam Compiler Kit.
 *
 * Permission to use,  ll, duplicate or disclose this software must be
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

#include	<pc_file.h>

extern		_rf();
extern		_incpt();

int _rdc(f) struct file *f; {
	int c;

	_rf(f);
	c = *f->ptr;
	_incpt(f);
	return(c);
}
