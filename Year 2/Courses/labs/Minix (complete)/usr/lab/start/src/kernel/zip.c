/* This file contains the device dependent part of a driver for the IBM-AT
 * winchester controller.
 * It was written by Adri Koppes.
 *
 * This driver supports the following message in m2 format:
 *
 *    m_type      DEVICE    PROC_NR     COUNT    POSITION  ADRRESS 
 * ----------------------------------------------------------------
 * |  DEV_IOCTL | device  | proc nr |func code|         | buf ptr |
 * ----------------------------------------------------------------
 *
 * The file contains one entry point:
 *
 *   zip_task:	main entry when system is brought up
 *
 * Notes:
 * 
 * - Although you can lock a removeable ATAPI device from userland by sending
 *   a DIOCLOCK, the task itself does not do any locking of the drive otherwise
 *   (i.e. during accesses). This is because some drives remember the last time
 *   the eject button was pressed. So if you lock the drive, from userland, and
 *   then restart the system without unlocking it, when the freshly booted
 *   kernel tries to unlock the drive after its first operation it will eject
 *   the disk due to memory. This can (and has) caused fs corruption.
 *
 * Changes:
 *	13 Apr 1992 by Kees J. Bot: device dependent/independent split.
 *	14 May 2000 by Kees J. Bot: d-d/i rewrite.
 *	23 Mar 2000 by Michael Temari: added ATAPI CDROM support.
 *  	25 Oct 2001 by Jacob Bower (jacob.bower@ic.ac.uk), 
 *		       Tim Wood: hacked in ATAPI ZIP support.
 *			
 */

#include "kernel.h"
#include "driver.h"
#include "drvlib.h"
#include <sys/ioctl.h>

#define ATAPI_DEBUG	   0 	/* To debug ATAPI code. */

/* I/O Ports used by winchester disk controllers. */

/* Read and write registers */
#define REG_BASE0	0x1F0	/* base register of controller 0 */
#define REG_BASE1	0x170	/* base register of controller 1 */
#define REG_DATA	    0	/* data register (offset from the base reg.) */
#define REG_PRECOMP	    1	/* start of write precompensation */
#define REG_COUNT	    2	/* sectors to transfer */
#define REG_SECTOR	    3	/* sector number */
#define REG_CYL_LO	    4	/* low byte of cylinder number */
#define REG_CYL_HI	    5	/* high byte of cylinder number */
#define REG_LDH		    6	/* lba, drive and head */
#define   LDH_DEFAULT		0xA0	/* ECC enable, 512 bytes per sector */
#define   LDH_LBA		0x40	/* Use LBA addressing */
#define   ldh_init(drive)	(LDH_DEFAULT | ((drive) << 4))

/* Read only registers */
#define REG_STATUS	    7	/* status */
#define   STATUS_BSY		0x80	/* controller busy */
#define	  STATUS_RDY		0x40	/* drive ready */
#define	  STATUS_WF		0x20	/* write fault */
#define	  STATUS_SC		0x10	/* seek complete (obsolete) */
#define	  STATUS_DRQ		0x08	/* data transfer request */
#define	  STATUS_CRD		0x04	/* corrected data */
#define	  STATUS_IDX		0x02	/* index pulse */
#define	  STATUS_ERR		0x01	/* error */
#define	  STATUS_ADMBSY	       0x100	/* administratively busy (software) */
#define REG_ERROR	    1	/* error code */
#define	  ERROR_BB		0x80	/* bad block */
#define	  ERROR_ECC		0x40	/* bad ecc bytes */
#define	  ERROR_ID		0x10	/* id not found */
#define	  ERROR_AC		0x04	/* aborted command */
#define	  ERROR_TK		0x02	/* track zero error */
#define	  ERROR_DM		0x01	/* no data address mark */

/* Write only registers */
#define REG_COMMAND	    7	/* command */
#define   CMD_IDLE		0x00	/* for w_command: drive idle */
#define   CMD_RECALIBRATE	0x10	/* recalibrate drive */
#define   CMD_READ		0x20	/* read data */
#define   CMD_WRITE		0x30	/* write data */
#define   CMD_READVERIFY	0x40	/* read verify */
#define   CMD_FORMAT		0x50	/* format track */
#define   CMD_SEEK		0x70	/* seek cylinder */
#define   CMD_DIAG		0x90	/* execute device diagnostics */
#define   CMD_SPECIFY		0x91	/* specify parameters */
#define   ATA_IDENTIFY		0xEC	/* identify drive */
#define REG_CTL		0x206	/* control register */
#define   CTL_NORETRY		0x80	/* disable access retry */
#define   CTL_NOECC		0x40	/* disable ecc retry */
#define   CTL_EIGHTHEADS	0x08	/* more than eight heads */
#define   CTL_RESET		0x04	/* reset controller */
#define   CTL_INTDISABLE	0x02	/* disable interrupts */

#define   ERROR_SENSE           0xF0    /* sense key mask */
#define     SENSE_NONE          0x00    /* no sense key */
#define     SENSE_RECERR        0x10    /* recovered error */
#define     SENSE_NOTRDY        0x20    /* not ready */
#define     SENSE_MEDERR        0x30    /* medium error */
#define     SENSE_HRDERR        0x40    /* hardware error */
#define     SENSE_ILRQST        0x50    /* illegal request */
#define     SENSE_UATTN         0x60    /* unit attention */
#define     SENSE_DPROT         0x70    /* data protect */
#define     SENSE_ABRT          0xb0    /* aborted command */
#define     SENSE_MISCOM        0xe0    /* miscompare */
#define   ERROR_MCR             0x08    /* media change requested */
#define   ERROR_ABRT            0x04    /* aborted command */
#define   ERROR_EOM             0x02    /* end of media detected */
#define   ERROR_ILI             0x01    /* illegal length indication */
#define REG_FEAT            1   /* features */
#define   FEAT_OVERLAP          0x02    /* overlap */
#define   FEAT_DMA              0x01    /* dma */
#define REG_IRR             2   /* interrupt reason register */
#define   IRR_REL               0x04    /* release */
#define   IRR_IO                0x02    /* direction for xfer */
#define   IRR_COD               0x01    /* command or data */
#define REG_SAMTAG          3
#define REG_CNT_LO          4   /* low byte of cylinder number */
#define REG_CNT_HI          5   /* high byte of cylinder number */
#define REG_DRIVE           6   /* drive select */
#define REG_STATUS          7   /* status */
#define   STATUS_BSY            0x80    /* controller busy */
#define   STATUS_DRDY           0x40    /* drive ready */
#define   STATUS_DMADF          0x20    /* dma ready/drive fault */
#define   STATUS_SRVCDSC        0x10    /* service or dsc */
#define   STATUS_DRQ            0x08    /* data transfer request */
#define   STATUS_CORR           0x04    /* correctable error occurred */
#define   STATUS_CHECK          0x01    /* check error */

#define   ATAPI_PACKETCMD       0xA0    /* packet command */
#define   ATAPI_IDENTIFY        0xA1    /* identify drive */

#define   SCSI_START_STOP	0x1B	/* used to eject disk from drive */
#define     SS_CLEAR		0x01	/* cancel an eject command */
#define     SS_EJECT		0x02	/* start stop command to eject */

#define   SCSI_READ10           0x28    /* read from disk */
#define   SCSI_LOCK		0x1E	/* lock/unlock drive */
#define   SCSI_WRITE10		0x2A	/* write to disk */
#define   SCSI_FORMAT_CAPACITY	0x23	/* inquire media size */
#define CD_SECTOR_SIZE		2048	/* sector size of a CD-ROM */

/* Interrupt request lines. */
#define NO_IRQ		 0	/* no IRQ set yet */
#define AT_IRQ0		14	/* interrupt number for controller 0 */
#define AT_IRQ1		15	/* interrupt number for controller 1 */

/* Common command block */
struct command {
  u8_t	precomp;	/* REG_PRECOMP, etc. */
  u8_t	count;
  u8_t	sector;
  u8_t	cyl_lo;
  u8_t	cyl_hi;
  u8_t	ldh;
  u8_t	command;
};


/* Error codes */
#define ERR		 (-1)	/* general error */
#define ERR_BAD_SECTOR	 (-2)	/* block marked bad detected */

/* Some controllers don't interrupt, the clock will wake us up. */
#define WAKEUP		(32*HZ)	/* drive may be out for 31 seconds max */

/* Miscellaneous. */
#define MAX_DRIVES         4	/* this driver supports 4 drives (d0 - d3) */
#if _WORD_SIZE > 2 
#define MAX_SECS	 256	/* controller can transfer this many sectors */
#else 
#define MAX_SECS	 127	/* but not to a 16 bit process */
#endif 
#define MAX_ERRORS         4	/* how often to try rd/wt before quitting */
#define NR_DEVICES      (MAX_DRIVES * DEV_PER_DRIVE)
#define SUB_PER_DRIVE	(NR_PARTITIONS * NR_PARTITIONS)
#define NR_SUBDEVS	(MAX_DRIVES * SUB_PER_DRIVE)
#define TIMEOUT        31500	/* controller timeout in ms */
#define RECOVERYTIME     500	/* controller recovery time in ms */
#define INITIALIZED	0x01	/* drive is initialized */
#define DEAF		0x02	/* controller must be reset */
#define SMART		0x04	/* drive supports ATA commands */
#define ATAPI		0x08	/* it is an ATAPI device */
#define ATAPIZIP	0x10	/* ATAPI Zip drive flag */
#define ATAPIUSERLOCK   0x20    /* ATAPI drive locked by user */
#define ATAPISYSLOCK    0x40	/* ATAPI drive locked due to operation */

/* Variables. */
PRIVATE struct wini {		/* main drive struct, one entry per drive */
  unsigned state;		/* drive state: deaf, initialized, dead */
  unsigned base;		/* base register of the register file */
  unsigned irq;			/* interrupt request line */
  unsigned lcylinders;		/* logical number of cylinders (BIOS) */
  unsigned lheads;		/* logical number of heads */
  unsigned lsectors;		/* logical number of sectors per track */
  unsigned pcylinders;		/* physical number of cylinders (translated) */
  unsigned pheads;		/* physical number of heads */
  unsigned psectors;		/* physical number of sectors per track */
  unsigned ldhpref;		/* top four bytes of the LDH (head) register */
  unsigned precomp;		/* write precompensation cylinder / 4 */
  unsigned max_count;		/* max request for this drive */
  unsigned open_ct;		/* in-use count */
  struct device part[DEV_PER_DRIVE];	/* disks and partitions */
  struct device subpart[SUB_PER_DRIVE];	/* subpartitions */
} wini[MAX_DRIVES], *w_wn;

PRIVATE int win_tasknr;			/* my task number */
PRIVATE int w_command;			/* current command in execution */
PRIVATE int w_status;			/* status after interrupt */
PRIVATE int w_drive;			/* selected drive */
PRIVATE struct device *w_dv;		/* device's base and size */

FORWARD _PROTOTYPE( void init_params, (void) );
FORWARD _PROTOTYPE( int w_do_open, (struct driver *dp, message *m_ptr) );
FORWARD _PROTOTYPE( struct device *w_prepare, (int device) );
FORWARD _PROTOTYPE( int w_identify, (void) );
FORWARD _PROTOTYPE( char *w_name, (void) );
FORWARD _PROTOTYPE( int w_specify, (void) );
FORWARD _PROTOTYPE( int w_transfer, (int proc_nr, int opcode, off_t position,
					iovec_t *iov, unsigned nr_req) );
FORWARD _PROTOTYPE( int com_out, (struct command *cmd) );
FORWARD _PROTOTYPE( void w_need_reset, (void) );
FORWARD _PROTOTYPE( int w_do_close, (struct driver *dp, message *m_ptr) );
FORWARD _PROTOTYPE( int com_simple, (struct command *cmd) );
FORWARD _PROTOTYPE( void w_timeout, (void) );
FORWARD _PROTOTYPE( int w_reset, (void) );
FORWARD _PROTOTYPE( void w_intr_wait, (void) );
FORWARD _PROTOTYPE( int at_intr_wait, (void) );
FORWARD _PROTOTYPE( int w_waitfor, (int mask, int value) );
FORWARD _PROTOTYPE( int w_handler, (int irq) );
FORWARD _PROTOTYPE( void w_geometry, (struct partition *entry) );
FORWARD _PROTOTYPE( int w_diocntl, (struct driver *dp, message *mp) );
FORWARD _PROTOTYPE( void atapi_lockdrive, (u8_t locked) );
FORWARD _PROTOTYPE( void atapi_ejectdrive, (u8_t ssmode) );
FORWARD _PROTOTYPE( unsigned long atapi_get_totalsize, (void) );
FORWARD _PROTOTYPE( int atapi_sendpacket, (u8_t *packet, unsigned cnt) );
FORWARD _PROTOTYPE( int atapi_intr_wait, (void) );
FORWARD _PROTOTYPE( int atapi_open, (int writemode) );
FORWARD _PROTOTYPE( void atapi_close, (void) );
FORWARD _PROTOTYPE( int atapi_transfer, (int proc_nr, int opcode,
			off_t position, iovec_t *iov, unsigned nr_req) );
FORWARD _PROTOTYPE( int atapi_cdrom_transfer, (int proc_nr, int opcode,
			off_t position, iovec_t *iov, unsigned nr_req) );

/* Entry points to this driver. */
PRIVATE struct driver w_dtab = {
  w_name,		/* current device's name */
  w_do_open,		/* open or mount request, initialize device */
  w_do_close,		/* release device */
  w_diocntl,		/* get or set a partition's geometry etc. */
  w_prepare,		/* prepare for I/O on a given minor device */
  w_transfer,		/* do the I/O */
  nop_cleanup,		/* nothing to clean up */
  w_geometry,		/* tell the geometry of the disk */
};


/*===========================================================================*
 *				zip_task			     *
 *===========================================================================*/
PUBLIC void zip_task() 
{
/* Set special disk parameters then call the generic main loop. */

  win_tasknr = proc_number(proc_ptr);

  init_params();

  driver_task(&w_dtab);
}


/*============================================================================*
 *				init_params				      *
 *============================================================================*/
PRIVATE void init_params()
{
/* This routine is called at startup to initialize the drive parameters. */

  u16_t parv[2];
  unsigned int vector;
  int drive, nr_drives;
  struct wini *wn;
  u8_t params[16];
  phys_bytes param_phys = vir2phys(params);

  /* Get the number of drives from the BIOS data area */
  phys_copy(0x475L, param_phys, 1L);
  if ((nr_drives = params[0]) > 2) nr_drives = 2;

  for (drive = 0, wn = wini; drive < MAX_DRIVES; drive++, wn++) {
	if (drive < nr_drives) {
		/* Copy the BIOS parameter vector */
		vector = drive == 0 ? WINI_0_PARM_VEC : WINI_1_PARM_VEC;
		phys_copy(vector * 4L, vir2phys(parv), 4L);

		/* Calculate the address of the parameters and copy them */
		phys_copy(hclick_to_physb(parv[1]) + parv[0], param_phys, 16L);

		/* Copy the parameters to the structures of the drive */
		wn->lcylinders = bp_cylinders(params);
		wn->lheads = bp_heads(params);
		wn->lsectors = bp_sectors(params);
		wn->precomp = bp_precomp(params) >> 2;
	}
	wn->ldhpref = ldh_init(drive);
	wn->max_count = MAX_SECS << SECTOR_SHIFT;

	/* Base I/O register to address controller. */
	wn->base = drive < 2 ? REG_BASE0 : REG_BASE1;
  }
}


/*============================================================================*
 *				w_do_open				      *
 *============================================================================*/
PRIVATE int w_do_open(dp, m_ptr)
struct driver *dp;
message *m_ptr;
{
/* Device open: Initialize the controller and read the partition table. */

  struct wini *wn;

  if (w_prepare(m_ptr->DEVICE) == NIL_DEV) return(ENXIO);
  wn = w_wn;

  if (wn->state == 0) {
	/* Try to identify the device. */
	if (w_identify() != OK) {
		printf("%s: probe failed\n", w_name());
		if (wn->state & DEAF) w_reset();
		wn->state = 0;
		return(ENXIO);
	}
  }
  if (wn->open_ct == 0) {
	if (wn->state & ATAPI)
		return atapi_open(m_ptr->COUNT & W_BIT);
	/* Partition the disk. */
	partition(&w_dtab, w_drive * DEV_PER_DRIVE, P_PRIMARY);
	wn->open_ct++;
  }
  return(OK);
}


/*===========================================================================*
 *				w_prepare				     *
 *===========================================================================*/
PRIVATE struct device *w_prepare(device)
int device;
{
/* Prepare for I/O on a device. */

  if (device < NR_DEVICES) {			/* d0, d0p[0-3], d1, ... */
	w_drive = device / DEV_PER_DRIVE;	/* save drive number */
	w_wn = &wini[w_drive];
	w_dv = &w_wn->part[device % DEV_PER_DRIVE];
  } else
  if ((unsigned) (device -= MINOR_d0p0s0) < NR_SUBDEVS) {/*d[0-7]p[0-3]s[0-3]*/
	w_drive = device / SUB_PER_DRIVE;
	w_wn = &wini[w_drive];
	w_dv = &w_wn->subpart[device % SUB_PER_DRIVE];
  } else {
	return(NIL_DEV);
  }
  return(w_dv);
}


/*===========================================================================*
 *				w_identify				     *
 *===========================================================================*/
PRIVATE int w_identify()
{
/* Find out if a device exists, if it is an old AT disk, or a newer ATA
 * drive, a removable media device, etc.
 */

  struct wini *wn = w_wn;
  struct command cmd;
  char id_string[40];
  int i, r;
  unsigned long size;
#define id_byte(n)	(&tmp_buf[2 * (n)])
#define id_word(n)	(((u16_t) id_byte(n)[0] <<  0) \
			|((u16_t) id_byte(n)[1] <<  8))
#define id_longword(n)	(((u32_t) id_byte(n)[0] <<  0) \
			|((u32_t) id_byte(n)[1] <<  8) \
			|((u32_t) id_byte(n)[2] << 16) \
			|((u32_t) id_byte(n)[3] << 24))

  /* Try to identify the device. */
  cmd.ldh     = wn->ldhpref;
  cmd.command = ATA_IDENTIFY;

  if (cmd.command = ATAPI_IDENTIFY, com_simple(&cmd) == OK) {
	/* An ATAPI device. */
	wn->state |= ATAPI;

	/* Device information. */
	port_read(wn->base + REG_DATA, tmp_phys, 512);

	/* 
	   String bytes are swapped because the data is sent in 16-bit
	   little-endian words. Infact in the case of ATAPI they are only
	   'Normally' little-endian, but there are few which aren't.
	   However Minix has no need to compensate for this.
	*/
	for (i = 0; i < 40; i++) id_string[i] = id_byte(27)[i^1];
	
	/* Dirty hack to detect IOMEGA ZIP drive. Note that if we don't detect
	   the drive to be a ZIP drive then we treat it as a CDROM for now. The
	   driver really needs to be cleaned up a little to allow it work in a
	   more generic way with ATAPI devices.
	*/
	if( !memcmp(id_string, "IOMEGA", 6) )
		wn->state |= ATAPIZIP;

	size = atapi_get_totalsize();
  } else
	/* If we detected no ATAPI devices, and we aren't allowing
	   detection of non ATAPI devices then...
	*/
	return (ERR);

	/* Size of the whole drive */
  wn->part[0].dv_size = mul64u(size, SECTOR_SIZE);

  if (w_specify() != OK && w_specify() != OK) return(ERR);

  printf("%s: ", w_name());
  if (wn->state & (SMART|ATAPI)) {
	printf("%.40s\n", id_string);
  } else {
	printf("%ux%ux%u\n", wn->pcylinders, wn->pheads, wn->psectors);
  }

  /* Everything looks OK; register IRQ so we can stop polling. */
  wn->irq = w_drive < 2 ? AT_IRQ0 : AT_IRQ1;
  put_irq_handler(wn->irq, w_handler);
  enable_irq(wn->irq);

  return(OK);
}

/*===========================================================================*
 *				w_name					     *
 *===========================================================================*/
PRIVATE char *w_name()
{
/* Return a name for the current device. */
  static char name[] = "at-d0";

  name[4] = '0' + w_drive;
  return name;
}


/*===========================================================================*
 *				w_specify				     *
 *===========================================================================*/
PRIVATE int w_specify()
{
/* Routine to initialize the drive after boot or when a reset is needed. */

  struct wini *wn = w_wn;
  struct command cmd;

  if ((wn->state & DEAF) && w_reset() != OK) return(ERR);

  if (!(wn->state & ATAPI)) {
	/* Specify parameters: precompensation, number of heads and sectors. */
	cmd.precomp = wn->precomp;
	cmd.count   = wn->psectors;
	cmd.ldh     = w_wn->ldhpref | (wn->pheads - 1);
	cmd.command = CMD_SPECIFY;		/* Specify some parameters */

	/* Output command block and see if controller accepts the parameters. */
	if (com_simple(&cmd) != OK) return(ERR);

	if (!(wn->state & SMART)) {
		/* Calibrate an old disk. */
		cmd.sector  = 0;
		cmd.cyl_lo  = 0;
		cmd.cyl_hi  = 0;
		cmd.ldh     = w_wn->ldhpref;
		cmd.command = CMD_RECALIBRATE;

		if (com_simple(&cmd) != OK) return(ERR);
	}
  }
  wn->state |= INITIALIZED;
  return(OK);
}


/*===========================================================================*
 *				w_transfer				     *
 *===========================================================================*/
PRIVATE int w_transfer(proc_nr, opcode, position, iov, nr_req)
int proc_nr;			/* process doing the request */
int opcode;			/* DEV_GATHER or DEV_SCATTER */
off_t position;			/* offset on device to read or write */
iovec_t *iov;			/* pointer to read or write request vector */
unsigned nr_req;		/* length of request vector */
{
  struct wini *wn = w_wn;
  iovec_t *iop, *iov_end = iov + nr_req;
  int r, errors;
  unsigned long block;
  unsigned long dv_size = cv64ul(w_dv->dv_size);
  struct command cmd;
  unsigned cylinder, head, sector, nbytes, count, chunk;
  unsigned secspcyl = wn->pheads * wn->psectors;
  phys_bytes user_base = proc_vir2phys(proc_addr(proc_nr), 0);

  if (w_wn->state & ATAPI) {
	return atapi_transfer(proc_nr, opcode, position, iov, nr_req);
  }

  /* Check disk address. */
  if ((position & SECTOR_MASK) != 0) return(EINVAL);

  errors = 0;

  while (nr_req > 0) {
	/* How many bytes to transfer? */
	nbytes = 0;
	for (iop = iov; iop < iov_end; iop++) nbytes += iop->iov_size;
	if ((nbytes & SECTOR_MASK) != 0) return(EINVAL);

	/* Which block on disk and how close to EOF? */
	if (position >= dv_size) return(OK);		/* At EOF */
	if (position + nbytes > dv_size) nbytes = dv_size - position;
	block = div64u(add64ul(w_dv->dv_base, position), SECTOR_SIZE);

	if (nbytes >= wn->max_count) {
		/* The drive can't do more then max_count at once. */
		nbytes = wn->max_count;
	}

	/* First check to see if a reinitialization is needed. */
	if (!(wn->state & INITIALIZED) && w_specify() != OK) return(EIO);

	/* Tell the controller to transfer nbytes bytes. */
	cmd.precomp = wn->precomp;
	cmd.count   = (nbytes >> SECTOR_SHIFT) & BYTE;
	if (wn->ldhpref & LDH_LBA) {
		cmd.sector  = (block >>  0) & 0xFF;
		cmd.cyl_lo  = (block >>  8) & 0xFF;
		cmd.cyl_hi  = (block >> 16) & 0xFF;
		cmd.ldh     = wn->ldhpref | ((block >> 24) & 0xF);
	} else {
		cylinder = block / secspcyl;
		head = (block % secspcyl) / wn->psectors;
		sector = block % wn->psectors;
		cmd.sector  = sector + 1;
		cmd.cyl_lo  = cylinder & BYTE;
		cmd.cyl_hi  = (cylinder >> 8) & BYTE;
		cmd.ldh     = wn->ldhpref | head;
	}
	cmd.command = opcode == DEV_SCATTER ? CMD_WRITE : CMD_READ;

	r = com_out(&cmd);

	while (r == OK && nbytes > 0) {
		/* For each sector, wait for an interrupt and fetch the data
		 * (read), or supply data to the controller and wait for an
		 * interrupt (write).
		 */

		if (opcode == DEV_GATHER) {
			/* First an interrupt, then data. */
			if ((r = at_intr_wait()) != OK) {
				/* An error, send data to the bit bucket. */
				if (w_status & STATUS_DRQ) {
					port_read(w_wn->base + REG_DATA,
						tmp_phys, SECTOR_SIZE);
				}
				break;
			}
		}

		/* Wait for data transfer requested. */
		if (!w_waitfor(STATUS_DRQ, STATUS_DRQ)) { r = ERR; break; }

		/* Copy bytes to or from the device's buffer. */
		if (opcode == DEV_GATHER) {
			port_read(w_wn->base + REG_DATA,
				user_base + iov->iov_addr, SECTOR_SIZE);
		} else {
			port_write(w_wn->base + REG_DATA,
				user_base + iov->iov_addr, SECTOR_SIZE);

			/* Data sent, wait for an interrupt. */
			if ((r = at_intr_wait()) != OK) break;
		}

		/* Book the bytes successfully transferred. */
		nbytes -= SECTOR_SIZE;
		position += SECTOR_SIZE;
		iov->iov_addr += SECTOR_SIZE;
		if ((iov->iov_size -= SECTOR_SIZE) == 0) { iov++; nr_req--; }
	}

	/* Any errors? */
	if (r != OK) {
		/* Don't retry if sector marked bad or too many errors. */
		if (r == ERR_BAD_SECTOR || ++errors == MAX_ERRORS) {
			w_command = CMD_IDLE;
			return(EIO);
		}
	}
  }

  w_command = CMD_IDLE;
  return(OK);
}


/*============================================================================*
 *				com_out					      *
 *============================================================================*/
PRIVATE int com_out(cmd)
struct command *cmd;		/* Command block */
{
/* Output the command block to the winchester controller and return status */

  struct wini *wn = w_wn;
  unsigned base = wn->base;

  if (!w_waitfor(STATUS_BSY, 0)) {
	printf("%s: controller not ready\n", w_name());
	return(ERR);
  }

  /* Select drive. */
  out_byte(base + REG_LDH, cmd->ldh);

  if (!w_waitfor(STATUS_BSY, 0)) {
	printf("%s: drive not ready\n", w_name());
	return(ERR);
  }

  /* Schedule a wakeup call, some controllers are flaky. */
  clock_mess(WAKEUP, w_timeout);

  out_byte(base + REG_CTL, wn->pheads >= 8 ? CTL_EIGHTHEADS : 0);
  out_byte(base + REG_PRECOMP, cmd->precomp);
  out_byte(base + REG_COUNT, cmd->count);
  out_byte(base + REG_SECTOR, cmd->sector);
  out_byte(base + REG_CYL_LO, cmd->cyl_lo);
  out_byte(base + REG_CYL_HI, cmd->cyl_hi);
  w_status = STATUS_ADMBSY;
  out_byte(base + REG_COMMAND, w_command = cmd->command);
  return(OK);
}


/*===========================================================================*
 *				w_need_reset				     *
 *===========================================================================*/
PRIVATE void w_need_reset()
{
/* The controller needs to be reset. */
  struct wini *wn;

  for (wn = wini; wn < &wini[MAX_DRIVES]; wn++) {
	wn->state |= DEAF;
	wn->state &= ~INITIALIZED;
  }
}


/*============================================================================*
 *				w_do_close				      *
 *============================================================================*/
PRIVATE int w_do_close(dp, m_ptr)
struct driver *dp;
message *m_ptr;
{
/* Device close: Release a device. */

  if (w_prepare(m_ptr->DEVICE) == NIL_DEV) return(ENXIO);
  w_wn->open_ct--;
  if (w_wn->open_ct == 0 && (w_wn->state & ATAPI)) atapi_close();
  return(OK);
}


/*============================================================================*
 *				com_simple				      *
 *============================================================================*/
PRIVATE int com_simple(cmd)
struct command *cmd;		/* Command block */
{
/* A simple controller command, only one interrupt and no data-out phase. */
  int r;

  if ((r = com_out(cmd)) == OK) r = at_intr_wait();
  w_command = CMD_IDLE;
  return(r);
}


/*===========================================================================*
 *				w_timeout				     *
 *===========================================================================*/
PRIVATE void w_timeout()
{
  struct wini *wn = w_wn;

  switch (w_command) {
  case CMD_IDLE:
	break;		/* fine */
  case CMD_READ:
  case CMD_WRITE:
	/* Impossible, but not on PC's:  The controller does not respond. */

	/* Limiting multisector I/O seems to help. */
	if (wn->max_count > 8 * SECTOR_SIZE) {
		wn->max_count = 8 * SECTOR_SIZE;
	} else {
		wn->max_count = SECTOR_SIZE;
	}
	/*FALL THROUGH*/
  default:
	/* Some other command. */
	printf("%s: timeout on command %02x\n", w_name(), w_command);
	w_need_reset();
	w_status = 0;
	interrupt(win_tasknr);
  }
}


/*===========================================================================*
 *				w_reset					     *
 *===========================================================================*/
PRIVATE int w_reset()
{
/* Issue a reset to the controller.  This is done after any catastrophe,
 * like the controller refusing to respond.
 */

  struct wini *wn;

  /* Wait for any internal drive recovery. */
  milli_delay(RECOVERYTIME);

  /* Strobe reset bit */
  out_byte(w_wn->base + REG_CTL, CTL_RESET);
  milli_delay(1);
  out_byte(w_wn->base + REG_CTL, 0);
  milli_delay(1);

  /* Wait for controller ready */
  if (!w_waitfor(STATUS_BSY, 0)) {
	printf("%s: reset failed, drive busy\n", w_name());
	return(ERR);
  }

  /* The error register should be checked now, but some drives mess it up. */

  for (wn = wini; wn < &wini[MAX_DRIVES]; wn++) {
	if (wn->base == w_wn->base) wn->state &= ~DEAF;
  }
  return(OK);
}


/*============================================================================*
 *				w_intr_wait				      *
 *============================================================================*/
PRIVATE void w_intr_wait()
{
/* Wait for a task completion interrupt. */

  message mess;

  if (w_wn->irq != NO_IRQ) {
	/* Wait for an interrupt that sets w_status to "not busy". */
	while (w_status & (STATUS_ADMBSY|STATUS_BSY)) receive(HARDWARE, &mess);
  } else {
	/* Interrupt not yet allocated; use polling. */
	(void) w_waitfor(STATUS_BSY, 0);
  }
}


/*============================================================================*
 *				at_intr_wait				      *
 *============================================================================*/
PRIVATE int at_intr_wait()
{
/* Wait for an interrupt, study the status bits and return error/success. */
  int r;

  w_intr_wait();
  if ((w_status & (STATUS_BSY | STATUS_WF | STATUS_ERR)) == 0) {
	r = OK;
  } else
  if ((w_status & STATUS_ERR) && (in_byte(w_wn->base + REG_ERROR) & ERROR_BB)) {
  	r = ERR_BAD_SECTOR;	/* sector marked bad, retries won't help */
  } else {
  	r = ERR;		/* any other error */
  }
  w_status |= STATUS_ADMBSY;	/* assume still busy with I/O */
  return(r);
}


/*==========================================================================*
 *				w_waitfor				    *
 *==========================================================================*/
PRIVATE int w_waitfor(mask, value)
int mask;			/* status mask */
int value;			/* required status */
{
/* Wait until controller is in the required state.  Return zero on timeout. */

  struct milli_state ms;

  milli_start(&ms);
  do {
       w_status = in_byte(w_wn->base + REG_STATUS);
       if ((w_status & mask) == value) return 1;
  } while (milli_elapsed(&ms) < TIMEOUT);

  w_need_reset();	/* Controller gone deaf. */
  return(0);
}


/*==========================================================================*
 *				w_handler				    *
 *==========================================================================*/
PRIVATE int w_handler(irq)
int irq;
{
/* Disk interrupt, send message to winchester task and reenable interrupts. */

  w_status = in_byte(w_wn->base + REG_STATUS);	/* acknowledge interrupt */
  interrupt(win_tasknr);
  return 1;
}


/*============================================================================*
 *				w_geometry				      *
 *============================================================================*/
PRIVATE void w_geometry(entry)
struct partition *entry;
{
  struct wini *wn = w_wn;

  if (wn->state & ATAPI) {		/* Make up some numbers. */
	entry->cylinders = div64u(wn->part[0].dv_size, SECTOR_SIZE) / (64*32);
	entry->heads = 64;
	entry->sectors = 32;
  } else {				/* Return logical geometry. */
	entry->cylinders = wn->lcylinders;
	entry->heads = wn->lheads;
	entry->sectors = wn->lsectors;
  }
}

/*============================================================================*
 *				w_diocntl				      *
 *============================================================================*/
PRIVATE int w_diocntl(dp, mp)
struct driver *dp;
message *mp;			/* pointer to ioctl request */
{
/* Carry out a partition setting/getting request. */
  struct device *dv;
  phys_bytes user_phys, entry_phys;
  struct partition entry;

  /* Decode the message parameters. */
  if ((dv = w_prepare(mp->DEVICE)) == NIL_DEV) return(ENXIO);

  user_phys = numap(mp->PROC_NR, (vir_bytes) mp->ADDRESS, sizeof(entry));
  if (user_phys == 0) return(EFAULT);

  entry_phys = vir2phys(&entry);

  switch(mp->REQUEST) {
	case DIOCEJECT:
		if( !(w_wn->state & ATAPIUSERLOCK) )
			atapi_ejectdrive(SS_EJECT);
		else
			return (EACCES);
		break;
		
	case DIOCLOCK:
		w_wn->state |= ATAPIUSERLOCK;
		atapi_lockdrive(1);
		break;
	
	case DIOCUNLOCK:
		w_wn->state &= ~ATAPIUSERLOCK;
		atapi_lockdrive(0);
		break;
			
  	case DIOCSETP:
		/* Copy just this one partition table entry. */
		phys_copy(user_phys, entry_phys, (phys_bytes) sizeof(entry));
		dv->dv_base = entry.base;
		dv->dv_size = entry.size;
		break;
		
  	case DIOCGETP:
		/* Return a partition table entry and the geometry of the drive. */
		entry.base = dv->dv_base;
		entry.size = dv->dv_size;
		w_geometry(&entry);
		phys_copy(entry_phys, user_phys, (phys_bytes) sizeof(entry));
		break;
	
	default:
		return (ENXIO);
  }
  
  return(OK);
}

/*===========================================================================*
 *				atapi_get_totalsize			     *
 *===========================================================================*/
PRIVATE unsigned long atapi_get_totalsize(void)
{
	u8_t packet[12];

	/* Build an ATAPI command packet. */
	packet[0] = SCSI_FORMAT_CAPACITY;
	packet[1] = 0;
	packet[2] = 0;
	packet[3] = 0;
	packet[4] = 0;
	packet[5] = 0;
	packet[6] = 0;
	packet[7] = 0;
	packet[8] = 12;
	packet[9] = 0;
	packet[10] = 0;
	packet[11] = 0;

	/* Tell the controller to execute the packet command. */
	atapi_sendpacket(packet, 12);

	if(atapi_intr_wait() == ERR)
		return 0;
	port_read(w_wn->base + REG_DATA,
		   vir2phys(packet), 12);

	return (packet[4]<<24) | (packet[5]<<16) | 
		(packet[6]<<8) | packet[7];
}

/*===========================================================================*
 *				atapi_lockdrive				     *
 *===========================================================================*/
PRIVATE void atapi_lockdrive(u8_t locked)
{
	u8_t packet[12];

	/* Build an ATAPI command packet. */
	packet[0] = SCSI_LOCK;
	packet[1] = 0;
	packet[2] = 0;
	packet[3] = 0;
	packet[4] = locked;
	packet[5] = 0;
	packet[6] = 0;
	packet[7] = 0;
	packet[8] = 0;
	packet[9] = 0;
	packet[10] = 0;
	packet[11] = 0;

	/* Tell the controller to execute the packet command. */
	atapi_sendpacket(packet, 0);
}

/*===========================================================================*
 *				atapi_ejectdrive				     *
 *===========================================================================*/
PRIVATE void atapi_ejectdrive(u8_t ssmode)
{
	u8_t packet[12];

	/* Build an ATAPI command packet. */
	packet[0] = SCSI_START_STOP;
	packet[1] = 0;
	packet[2] = 0;
	packet[3] = 0;
	packet[4] = ssmode;
	packet[5] = 0;
	packet[6] = 0;
	packet[7] = 0;
	packet[8] = 0;
	packet[9] = 0;
	packet[10] = 0;
	packet[11] = 0;

	/* Tell the controller to execute the packet command. */
	atapi_sendpacket(packet, 0);
}

/*===========================================================================*
 *				atapi_open				     *
 *===========================================================================*/
PRIVATE int atapi_open(int writemode)
{
  unsigned long total_size;
  if(writemode && !(w_wn->state & ATAPIZIP))
	return (ENXIO);

  /* sometimes we read 0 first time because drive not reset/initalise yet */
  total_size = atapi_get_totalsize();
  if(!total_size)
  	total_size = atapi_get_totalsize();
  if(!total_size)
  	total_size = 9999;
  w_wn->part[0].dv_size = mul64u(total_size, SECTOR_SIZE); 

  partition(&w_dtab, w_drive * DEV_PER_DRIVE, P_PRIMARY);
  w_wn->open_ct++;

  return(OK);
}

/*===========================================================================*
 *				atapi_close				     *
 *===========================================================================*/
PRIVATE void atapi_close()
{
}

/*===========================================================================*
 *				atapi_transfer				     *
 *===========================================================================*/
PRIVATE int atapi_transfer(proc_nr, opcode, position, iov, nr_req)
int proc_nr;			/* process doing the request */
int opcode;			/* DEV_GATHER or DEV_SCATTER */
off_t position;			/* offset on device to read or write */
iovec_t *iov;			/* pointer to read or write request vector */
unsigned nr_req;		/* length of request vector */
{
	iovec_t *iop, *iov_end = iov + nr_req;
	int r;
	unsigned long block;
	unsigned nbytes, nblocks;
	phys_bytes user_base = proc_vir2phys(proc_addr(proc_nr), 0);
	u8_t packet[12];
	u64_t part_pos;

	/* If you are trying to get any type of ATAPI device to work, that isn't
	   a CDROM, you should try removing this test. */
	if(!(w_wn->state & ATAPIZIP))
  		return atapi_cdrom_transfer(proc_nr, opcode, position, iov, nr_req);
  	
	/* Check disk address (only read on sector boundies). */
	if ((position & SECTOR_MASK) != 0) return(EINVAL);
	
	/* can't read outside of partition */
	if (position >= cv64ul(w_dv->dv_size)) return(EINVAL);

	/* We only read linear blocks as long as they fill whole sectors */
	nbytes = 0; 
	for (iop = iov; iop < iov_end; iop++){
		 if(iov->iov_size >= SECTOR_SIZE)
			 nbytes += iop->iov_size;
		 else
			break;
	}

	if(!nbytes)	return (EINVAL);

	nblocks = nbytes / SECTOR_SIZE;
	part_pos = add64ul(w_dv->dv_base, position);
	block = div64u(part_pos, SECTOR_SIZE);

	if(ATAPI_DEBUG)
		printf("%s req block: %lu + %u\n", (opcode == DEV_GATHER)?"Read":"Write", block, nblocks);

	/* First check to see if a reinitialization is needed. */
	if (!(w_wn->state & INITIALIZED) && w_specify() != OK) return(EIO);

	/* Build an ATAPI command packet. */
	packet[0] = (opcode == DEV_GATHER) ? SCSI_READ10 : SCSI_WRITE10;
	packet[1] = 0;
	packet[2] = (block >> 24) & 0xFF;
	packet[3] = (block >> 16) & 0xFF;
	packet[4] = (block >>  8) & 0xFF;
	packet[5] = (block >>  0) & 0xFF;
	packet[6] = 0;
	packet[7] = (nblocks >> 8) & 0xFF;
	packet[8] = (nblocks >> 0) & 0xFF;
	packet[9] = 0;
	packet[10] = 0;
	packet[11] = 0;

	/* Tell the controller to execute the packet command. */
	r = atapi_sendpacket(packet, nblocks * SECTOR_SIZE);
	if (r != OK) return (EIO);

	/* Write chunks of data */
	if(opcode == DEV_SCATTER) {
		milli_delay(10);
		while(nbytes > 0 && iov < iov_end) {
			if(iov->iov_size >= SECTOR_SIZE) {
				port_write(w_wn->base + REG_DATA,
					   user_base + iov->iov_addr, SECTOR_SIZE);
				if((r = atapi_intr_wait()) == ERR) break;
				iov->iov_size -= SECTOR_SIZE;
				iov->iov_addr += SECTOR_SIZE;	
				nbytes -= SECTOR_SIZE;
			} else
				iov++;
		} 
	}

	/* Read Chunks of data */
	if(opcode == DEV_GATHER) {
		while(nbytes > 0 && iov < iov_end) {
			if(iov->iov_size >= SECTOR_SIZE) {
				if((r = atapi_intr_wait()) != SECTOR_SIZE)
					break;
				port_read(w_wn->base + REG_DATA,
					   user_base + iov->iov_addr, SECTOR_SIZE);
				iov->iov_size -= SECTOR_SIZE;
				iov->iov_addr += SECTOR_SIZE;	
				nbytes -= SECTOR_SIZE;
			} else
				iov++;
		} 
	}

	w_command = CMD_IDLE;

	return ((r == ERR)?EIO:OK);
}

/*===========================================================================*
 *                       atapi_cdrom_transfer                                *
 *                                                                           *
 * This function is slightly different from atapi_transfer as it deals with  *
 * the fact that CDROMs have a sector size of 2048 bytes (rather than 512).  *
 * Idealy the two functions should be mereged, with proper detection of the  *
 * sector sizes.                                                             *
 *===========================================================================*/
PRIVATE int atapi_cdrom_transfer(proc_nr, opcode, position, iov, nr_req)
int proc_nr;			/* process doing the request */
int opcode;			/* DEV_GATHER or DEV_SCATTER */
off_t position;			/* offset on device to read or write */
iovec_t *iov;			/* pointer to read or write request vector */
unsigned nr_req;		/* length of request vector */
{
  struct wini *wn = w_wn;
  iovec_t *iop, *iov_end = iov + nr_req;
  int r, errors, fresh;
  u64_t pos;
  unsigned long block;
  unsigned long dv_size = cv64ul(w_dv->dv_size);
  unsigned nbytes, nblocks, count, before, chunk;
  phys_bytes user_base = proc_vir2phys(proc_addr(proc_nr), 0);
  u8_t packet[12];

  errors = fresh = 0;

  while (nr_req > 0 && !fresh) {
	/* The Minix block size is smaller than the CD block size, so we
	 * may have to read extra before or after the good data.
	 */
	pos = add64ul(w_dv->dv_base, position);
	block = div64u(pos, CD_SECTOR_SIZE);
	before = rem64u(pos, CD_SECTOR_SIZE);

	/* How many bytes to transfer? */
	nbytes = count = 0;
	for (iop = iov; iop < iov_end; iop++) {
		nbytes += iop->iov_size;
		if ((before + nbytes) % CD_SECTOR_SIZE == 0) count = nbytes;
	}

	/* Does one of the memory chunks end nicely on a CD sector multiple? */
	if (count != 0) nbytes = count;

	/* Data comes in as words, so we have to enforce even byte counts. */
	if ((before | nbytes) & 1) return(EINVAL);

	/* Which block on disk and how close to EOF? */
	if (position >= dv_size) return(OK);		/* At EOF */
	if (position + nbytes > dv_size) nbytes = dv_size - position;

	nblocks = (before + nbytes + CD_SECTOR_SIZE - 1) / CD_SECTOR_SIZE;
	if (ATAPI_DEBUG) {
		printf("block=%lu, before=%u, nbytes=%u, nblocks=%u\n",
			block, before, nbytes, nblocks);
	}

	/* First check to see if a reinitialization is needed. */
	if (!(wn->state & INITIALIZED) && w_specify() != OK) return(EIO);

	/* Build an ATAPI command packet. */
	packet[0] = SCSI_READ10;
	packet[1] = 0;
	packet[2] = (block >> 24) & 0xFF;
	packet[3] = (block >> 16) & 0xFF;
	packet[4] = (block >>  8) & 0xFF;
	packet[5] = (block >>  0) & 0xFF;
	packet[7] = (nblocks >> 8) & 0xFF;
	packet[8] = (nblocks >> 0) & 0xFF;
	packet[9] = 0;
	packet[10] = 0;
	packet[11] = 0;

	/* Tell the controller to execute the packet command. */
	r = atapi_sendpacket(packet, nblocks * CD_SECTOR_SIZE);
	if (r != OK) goto err;

	/* Read chunks of data. */
	while ((r = atapi_intr_wait()) > 0) {
		count = r;

		if (ATAPI_DEBUG) {
			printf("before=%u, nbytes=%u, count=%u\n",
				before, nbytes, count);
		}

		while (before > 0 && count > 0) {	/* Discard before. */
			chunk = before;
			if (chunk > count) chunk = count;
			if (chunk > DMA_BUF_SIZE) chunk = DMA_BUF_SIZE;
			port_read(w_wn->base + REG_DATA, tmp_phys, chunk);
			before -= chunk;
			count -= chunk;
		}

		while (nbytes > 0 && count > 0) {	/* Requested data. */
			chunk = nbytes;
			if (chunk > count) chunk = count;
			if (chunk > iov->iov_size) chunk = iov->iov_size;
			port_read(w_wn->base + REG_DATA,
					user_base + iov->iov_addr, chunk);
			position += chunk;
			nbytes -= chunk;
			count -= chunk;
			iov->iov_addr += chunk;
			fresh = 0;
			if ((iov->iov_size -= chunk) == 0) {
				iov++;
				nr_req--;
				fresh = 1;	/* new element is optional */
			}
		}

		while (count > 0) {		/* Excess data. */
			chunk = count;
			if (chunk > DMA_BUF_SIZE) chunk = DMA_BUF_SIZE;
			port_read(w_wn->base + REG_DATA, tmp_phys, chunk);
			count -= chunk;
		}
	}

	if (r < 0) {
  err:		/* Don't retry if too many errors. */
		if (++errors == MAX_ERRORS) {
			w_command = CMD_IDLE;
			return(EIO);
		}
	}
  }

  w_command = CMD_IDLE;
  return(OK);
}
/*===========================================================================*
 *				atapi_sendpacket			     *
 *===========================================================================*/
PRIVATE int atapi_sendpacket(packet, cnt)
u8_t *packet;
unsigned cnt;
{
/* Send an Atapi Packet Command */
  struct wini *wn = w_wn;
  message mess;
  int retries;

  /* There are two artifical delays in this function, this is to compensate
     for buggy hardware that does not set ready status bits at the correct
     times (it says its ready when its not!). However this introduces a 200ms
     delay with every block read/write. Somebody please !!FIXME!! */
  milli_delay(100);

   /* Select Master/Slave drive */
  out_byte(wn->base + REG_DRIVE, wn->ldhpref);

  if (!w_waitfor(STATUS_BSY | STATUS_DRQ, 0)) {
	printf("%s: timeout waiting for drive to come ready\n", w_name());
	return(ERR);
  }

  /* Schedule wakeup call. */
  clock_mess(WAKEUP, w_timeout);

#if _WORD_SIZE > 2 
  if (cnt > 0xFFFE) cnt = 0xFFFE;	/* Max data per interrupt. */
#endif 

  out_byte(wn->base + REG_FEAT, 0);
  out_byte(wn->base + REG_IRR, 0);
  out_byte(wn->base + REG_SAMTAG, 0);
  out_byte(wn->base + REG_CNT_LO, (cnt >> 0) & 0xFF);
  out_byte(wn->base + REG_CNT_HI, (cnt >> 8) & 0xFF);
  out_byte(wn->base + REG_COMMAND, w_command = ATAPI_PACKETCMD);

  if (!w_waitfor(STATUS_BSY | STATUS_DRQ, STATUS_DRQ)) {
	printf("%s: timeout (BSY|DRQ -> DRQ)\n", w_name());
	return(ERR);
  }

  w_status |= STATUS_ADMBSY;		/* Command not at all done yet. */

  /* Send the command packet to the device. */
  port_write(wn->base + REG_DATA, vir2phys(packet), 12);

  milli_delay(100);   

  return ( OK );
}

void atapi_dumpsense(void)
{
/*	packet[0] = 0x5A;
	packet[1] = 0;*/
	
}

/*============================================================================*
 *				atapi_intr_wait				      *
 *============================================================================*/
PRIVATE int atapi_intr_wait()
{
/* Wait for an interrupt and study the results.  Returns a number of bytes
 * that need to be transferred, or an error code.
 */
  struct wini *wn = w_wn;
  int e;
  int len;
  int irr;
  int r;
  int phase;

  w_intr_wait();

  e = in_byte(wn->base + REG_ERROR);
  len = in_byte(wn->base + REG_CNT_LO);
  len |= in_byte(wn->base + REG_CNT_HI) << 8;
  irr = in_byte(wn->base + REG_IRR);
  if (ATAPI_DEBUG) {
	printf("S=%02x E=%02x L=%04x I=%02x\n", w_status, e, len, irr);
  }

  if(e == 0x30)
  	return 512;

  if (w_status & (STATUS_BSY | STATUS_CHECK)) {
  	if(w_status & STATUS_BSY)
  		printf("ATAPI: Error, got busy\n");
  	else
  		atapi_dumpsense();
  	return ERR;
  }

  phase = (w_status & STATUS_DRQ) | (irr & (IRR_COD | IRR_IO));

  switch (phase) {
  case IRR_COD | IRR_IO:
	if (ATAPI_DEBUG) printf("ACD: Phase Command Complete\n");
	r = OK;
	break;
  case 0:
	if (ATAPI_DEBUG) printf("ACD: Phase Command Aborted\n");
	r = ERR;
	break;
  case STATUS_DRQ | IRR_COD:
	if (ATAPI_DEBUG) printf("ACD: Phase Command Out\n");
	r = ERR;
	break;
  case STATUS_DRQ:
	if (ATAPI_DEBUG) printf("ACD: Phase Data Out %d\n", len);
	r = len;
	break;
  case STATUS_DRQ | IRR_IO:
	if (ATAPI_DEBUG) printf("ACD: Phase Data In %d\n", len);
	r = len;
	break;
  default:
	if (ATAPI_DEBUG) printf("ACD: Phase Unknown\n");
	r = ERR;
	break;
  }

#if 0 
  /* retry if the media changed */
  XXX while (phase == (IRR_IO | IRR_COD) && (w_status & STATUS_CHECK)
	&& (e & ERROR_SENSE) == SENSE_UATTN && --try > 0);
#endif 

  w_status |= STATUS_ADMBSY;	/* Assume not done yet. */

  return(r);
}
