# The Makefile for the limited memory exercise
# Select optimisation to allow extra checks
OPT=-O
# ask for the recommended warnings
WARN=-Wall
# generate code with debugging 
CFLAGS = -g $(OPT) $(WARN) $(INCLUDES)
# to ma  
BIN   =       matmult
# change this to gcc-check to make gcc generate bound checking code
CC      = gcc

# Rules 
all       : $(BIN) 

mult.o    : mult.c machine.h

matmult.o : matmult.c machine.h

machine.o : machine.c machine.h


matmult   : matmult.o mult.o machine.o 
		$(CC) -o matmult matmult.o mult.o machine.o  -lm -g 

clean     :
	    /bin/rm -f *.o core a.out $(BIN)

