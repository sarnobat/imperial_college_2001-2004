/*
  (c) copyright 1988 by the Vrije Universiteit, Amsterdam, The Netherlands.
  See the copyright notice in the ACK home directory, in the file "Copyright".
*/

/*
  Module:	integer abs function
  Author:	Ceriel J.H. Jacobs
  Ver on:	$Header: absi.c,v 1.3 88/03/23 17:54:17 ceriel Exp $
*/

absi(i)
{
	return i >= 0 ? i : -i;
}
