/* Prototypes for system library functions. */

#ifndef _SYSLIB_H
#define _SYSLIB_H

#ifndef _TYPES_H
#include <sys/types.h>
#endif

/* Hide names to avoid name space pollution. */
#define sendrec		_sendrec
#define receive		_receive
#define send		_send

/* Minix user+system library. */
_PROTOTYPE( int printf, (const char *_fmt, ...)				);
_PROTOTYPE( void putk, (int _c)						);
_PROTOTYPE( int sendrec, (int _src_dest, message *_m_ptr)		);
_PROTOTYPE( int _taskcall, (int _who, int _syscallnr, message *_msgptr)	);

/* Minix system library. */
_PROTOTYPE( int receive, (int _src, message *_m_ptr)			);
_PROTOTYPE( int send, (int _dest, message *_m_ptr)			);

_PROTOTYPE( int sys_abort, (int _how, ...)				);
_PROTOTYPE( int sys_adjmap, (int _proc, struct mem_map *_ptr, 
		vir_clicks _data_clicks, vir_clicks _sp)		);
_PROTOTYPE( int sys_copy, (int _src_proc, int _src_seg, phys_bytes _src_vir, 
	int _dst_proc, int _dst_seg, phys_bytes _dst_vir, phys_bytes _bytes));
_PROTOTYPE( int sys_exec, (int _proc, char *_ptr, 
				char *_aout, vir_bytes _initpc)		);
_PROTOTYPE( int sys_execmap, (int _proc, struct mem_map *_ptr)		);
_PROTOTYPE( int sys_fork, (int _parent, int _child, int _pid)		);
_PROTOTYPE( int sys_getsp, (int _proc, vir_bytes *_newsp)		);
_PROTOTYPE( int sys_newmap, (int _proc, struct mem_map *_ptr)		);
_PROTOTYPE( int sys_getmap, (int _proc, struct mem_map *_ptr)		);
_PROTOTYPE( int sys_sendsig, (int _proc, struct sigmsg *_ptr)		);
_PROTOTYPE( int sys_oldsig, (int _proc, int _sig, sighandler_t _sighandler));
_PROTOTYPE( int sys_endsig, (int _proc)					);
_PROTOTYPE( int sys_sigreturn, (int _proc, vir_bytes _scp, int _flags)	);
_PROTOTYPE( int sys_xit, (int _parent, int _proc)			);
_PROTOTYPE( int sys_kill, (int _proc, int _sig)				);
_PROTOTYPE( int sys_times, (int _proc, clock_t _ptr[5])			);
_PROTOTYPE( int sys_sysctl, (int _proc, int _request, int priv,
						vir_bytes _argp)	);
_PROTOTYPE( int sys_findproc, (char *_name, int *_proc_nr, int _flags)	);

#endif /* _SYSLIB_H */
