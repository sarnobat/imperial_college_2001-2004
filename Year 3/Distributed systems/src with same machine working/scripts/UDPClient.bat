@echo off
.\timer -f "\t%%E elapsed, \t%%S sys, \t%%U user, \t%%M mem, \t%%r I/O recv, \t%%s  O sent" java -cp udp UDPClient %*