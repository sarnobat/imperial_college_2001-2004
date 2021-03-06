/*
 * fprintf - write output on a stream
 */
/* $Header: fprintf.c,v 1.3 89/12/18 15:01:54 eck Exp $ */

#include	<stdio.h>
#include	<stdarg.h>
#include	"loc_incl.h"

int
fprintf(FILE *stream, const char *format, ...)
{
	v list ap;
	int retval;
	
	va_start(ap, format);

	retval = _doprnt (format, ap, stream);

	va_end(ap);

	return retval;
}
