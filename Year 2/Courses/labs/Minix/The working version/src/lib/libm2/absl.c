/*
  (c) copyright 1988 by the Vrije Universiteit, Amsterdam, The Netherlands.
  See the copyright notice in the ACK home directory, in the file "Copyright".
*/

/*
  Module:	longint abs function
  Author:	Ceriel J.H. Jacobs
  Ver on:	$Header: absl.c,v 1.3 88/03/23 17:54:27 ceriel Exp $
*/
long
absl(i)
	long i;
{
	return i >= 0 ? i : -i;
}
