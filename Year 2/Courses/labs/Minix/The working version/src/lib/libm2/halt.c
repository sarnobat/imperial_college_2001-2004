/*
  (c) copyright 1988 by the Vrije Universiteit, Amsterdam, The Netherlands.
  See the copyright notice in the ACK home directory, in the file "Copyright".
*/

/*
  Module:	program termination routines
  Author:	Ceriel J.H. Jaco 
  Version:	$Header: halt.c,v 1.11 91/03/15 09:24:03 ceriel Exp $
*/
#define MAXPROCS 32

static int callindex = 0;
static int (*proclist[MAXPROCS])();

_cleanup()
{
	while (--callindex >= 0)
		(*proclist[callindex])();
	callindex = 0;
}

CallAtEnd(p)
	int (*p)();
{
	if (callindex >= MAXPROCS) {
		return 0;
	}
	proclist[callindex++] = p;
	return 1;
}

halt()
{
	_cleanup();
	_exit(0);
}
