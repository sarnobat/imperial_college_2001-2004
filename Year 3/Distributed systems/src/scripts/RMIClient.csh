#!/bin/csh

setenv SECPOLICY "file:./policy"

/usr/bin/time -f "\t%E elapsed, \t%S sys, \t  user, \t%M mem, \t%r I/O recv, \t%s I/O sent" java -cp rmi-client:rmi-common -Djava.security.policy=$SECPOLICY RMIClient $*
