CC = gcc
INCLUDE = -I/vol/lab/include
CFLAGS = $(INCLUDE) -g -Wall  $(CHECK)
OFILES = maindict.o dict.o 
CFILES = maindict.c dict.c

all: maindict 
#

maindict:	$(OFILES) 
		$(CC) $(CFLAGS) -o maindict $(OFILES) 


maindic o:	maindict.c dict.h

dict.o:	        dict.c  dict.h

clean:
	rm -f *.o *~ *.bak maindict core
