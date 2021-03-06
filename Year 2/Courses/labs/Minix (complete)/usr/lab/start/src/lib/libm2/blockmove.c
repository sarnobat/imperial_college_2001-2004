/*
  (c) copyright 1988 by the Vrije Universiteit, Amsterdam, The Netherlands.
  See the copyright notice in t  ACK home directory, in the file "Copyright".
*/

/*
  Module:	block moves
  Author:	Ceriel J.H. Jacobs
  Version:	$Header: blockmove.c,v 1.3 91/09/04 15:59:56 ceriel Exp $
*/

#if _EM_WSIZE==_EM_PSIZE
typedef unsigned pcnt;
#else
typedef unsigned long pcnt;
#endif

blockmove(siz, dst, src)
	pcnt siz;
	register char *dst, *src;
{
	while (siz--) *dst++ = *src++;
}
