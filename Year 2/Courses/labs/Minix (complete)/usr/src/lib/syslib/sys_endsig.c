#include "syslib.h"

PUBLIC int sys_endsig(proc)
int proc;
{
  message m;

  m.m1_i1 = proc;
  return( askcall(SYSTASK, SYS_ENDSIG, &m));
}
