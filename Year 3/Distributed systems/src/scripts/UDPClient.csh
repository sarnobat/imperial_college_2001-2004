#!/bin/csh

/usr/bin/time -f "\t%E elapsed, \t%S sys, \t%U user, \t%M mem, \t%r I/O recv, \t  I/O sent" java -cp udp UDPClient $*
