/* $Header: rcka.c,v 2.3 91/02/22 16:57:24 ceriel Exp $ */
/*
 * (c) copyright 1990 by the Vrije Universiteit, Amsterdam, The Netherlands.
 * See the copyright notice in the ACK home directory, in the file "Copyright".
 */

/* Aut r: Hans van Eck */

#include	<em_abs.h>

extern _trp();

struct array_descr	{
		int		lbound;
		unsigned	n_elts_min_one;
		unsigned	size;		/* doesn't really matter */
	    };

_rcka(descr, index)
struct array_descr *descr;
{
	if( index < descr->lbound ||
	    index > (int) descr->n_elts_min_one + descr->lbound )
		_trp(EARRAY);
}
