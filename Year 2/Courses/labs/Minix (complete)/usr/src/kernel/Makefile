# Makefile for kernel

# Directories
u = /usr
i = $u/include
s = $i/sys
h = $i/minix
b = $i/ibm
l = $u/lib

# Programs, flags, etc.
CC =	exec cc
CPP =	$l/cpp
LD =	$(CC) -.o
CFLAGS = -I$i
LDFLAGS = -i

HEAD =	mpx.o

OBJS =	start.o protect.o klib.o table.o main.o proc.o \
	exception.o system.o clock.o memory.o tty.o keyboard.o \
	console.o i8259.o rs232.o dmp.o misc.o driver.o \
	drvlib.o floppy.o printer.o pty.o zip.o debugger.o

# What to make.
kernel: $(HEAD) $(OBJS)
	$(LD) $(LDFLAGS) -o $@ $(HEAD) $(OBJS) -lsys
	install -S 0 $@

all install:
	cd keymaps && $(MAKE) -$(MAKEFLAGS) $@

clean:
	cd keymaps && $(MAKE) -$(MAKEFLAGS) $@
	rm -f *.o *.bak kernel

# Dependencies
a =	kernel.h $h/config.h $h/const.h $h/type.h $h/syslib.h \
	$s/types.h $i/string.h $i/limits.h $i/errno.h \
	const.h type.h proto.h glo.h
d =	driver.h $h/callnr.h $h/com.h $h/partition.h $h/u64.h proc.h
dl =	drvlib.h $b/partition.h

klib.o: $h/config.h $h/const.h const.h sconst.h protect.h
klib.o: klib.s 
mpx.o: $h/config.h $h/const.h $h/com.h const.h protect.h sconst.h
mpx.o: mpx.s 

clock.o:	$a
clock.o:	$i/signal.h
clock.o:	$h/callnr.h
clock.o:	$h/com.h
clock.o:	proc.h

console.o:	$a
console.o:	$i/termios.h
console.o:	$h/callnr.h
console.o:	$h/com.h
console.o:	protect.h
console.o:	tty.h
console.o:	proc.h

start.o:	$a
start.o:	$i/stdlib.h
start.o:	protect.h

dmp.o:		$a
dmp.o:		$h/com.h
dmp.o:		proc.h

exception.o:	$a
exception.o:	$i/signal.h
exception.o:	proc.h

driver.o:	$a $d
driver.o:	$s/ioctl.h

drvlib.o:	$a $d $(dl)

floppy.o:	$a $d $(dl)
floppy.o:	$b/diskparm.h

i8259.o:	$a

keyboard.o:	$a
keyboard.o:	$i/termios.h
keyboard.o:	$i/signal.h
keyboard.o:	$i/unistd.h
keyboard.o:	$h/callnr.h
keyboard.o:	$h/com.h
keyboard.o:	$h/keymap.h
keyboard.o:	tty.h
keyboard.o:	keymaps/us-std.src

main.o:	$a
main.o:	$i/unistd.h
main.o:	$i/signal.h
main.o:	$i/a.out.h
main.o:	$h/callnr.h
main.o:	$h/com.h
main.o:	proc.h

memory.o:	$a $d
memory.o:	$s/ioctl.h
memory.o:	protect.h
memory.o:	$b/int86.h

misc.o:	$a
misc.o:	$i/stdlib.h
misc.o:	$h/com.h
misc.o:	assert.h

printer.o:	$a
printer.o:	$h/callnr.h
printer.o:	$h/com.h
printer.o:	proc.h

proc.o:	$a
proc.o:	$h/callnr.h
proc.o:	$h/com.h
proc.o:	proc.h

protect.o:	$a
protect.o:	proc.h
protect.o:	protect.h

pty.o:	$a
pty.o:	$i/termios.h
pty.o:	$i/signal.h
pty.o:	$h/callnr.h
pty.o:	$h/com.h
pty.o:	tty.h
pty.o:	proc.h

rs232.o:	$a
rs232.o:	$i/termios.h
rs232.o:	$i/signal.h
rs232.o:	tty.h
rs232.o:	proc.h

system.o:	$a
system.o:	$i/stdlib.h
system.o:	$i/signal.h
system.o:	$i/unistd.h
system.o:	$s/sigcontext.h
system.o:	$s/svrctl.h
system.o:	$h/callnr.h
system.o:	$h/com.h
system.o:	proc.h
system.o:	protect.h
system.o:	assert.h

table.o:	$a
table.o:	$i/stdlib.h
table.o:	$i/termios.h
table.o:	$h/com.h
table.o:	proc.h
table.o:	tty.h
table.o:	$b/int86.h

tty.o:		$a
tty.o:		$i/termios.h
tty.o:		$i/sgtty.h
tty.o:		$s/ioctl.h
tty.o:		$i/signal.h
tty.o:		$h/callnr.h
tty.o:		$h/com.h
tty.o:		$h/keymap.h
tty.o:		proc.h
tty.o:		tty.h

zip.o:		$a $d $(dl)

debugger.o:	$a
debugger.o:	$i/stdlib.h
debugger.o:	$i/signal.h
debugger.o:	$h/callnr.h
debugger.o:	$h/com.h
debugger.o:	proc.h
