/*
 * (c) copyright 1988 by the Vrije Universiteit, Amsterdam, The Netherlands.
 * See the copyright notice in the ACK home directory, in the file "Copyright".
 *
 * Author: Ceriel J.H. Jacobs
 */
/* $Header: floor.c,v 1.1 89/05/1 16:01:29 eck Exp $ */

#include	<math.h>

double
floor(double x)
{
	double val;

	return modf(x, &val) < 0 ? val - 1.0 : val ;
	/*	this also works if modf always returns a positive
		fractional part
	*/
}
