ALL		=	cversion example assembler

all		:	$(ALL)

cversion	:	cversion.o
			gcc -o cversion cversion.o

cversion.o	:	cversion.c
			gcc -Wall -c cversion.c

example		: 	example.o
			gcc -o example example.o

example.o	:	examE e.c
			gcc -Wall -c example.c

assembler	:	assembler.o
			gcc -o assembler assembler.o

assembler.o	:	assembler.s
			as assembler.s -o assembler.o