@echo off
set SECPOLICY="file:./policy"
.\timer -f "\t%%E elapsed, \t%%S sys, \t%%U user, \t M mem, \t%%r I/O recv, \t%%s I/O sent" java -cp rmi-client;rmi-common -Djava.security.policy=%SECPOLICY% RMIClient %*
