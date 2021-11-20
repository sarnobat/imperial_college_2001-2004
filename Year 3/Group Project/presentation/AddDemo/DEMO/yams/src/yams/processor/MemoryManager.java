/*
 * Created on 26-Oct-2003
 *
 */
package yams.processor;
import java.io.PrintStream;
import java.util.TreeMap;
import yams.*;

public class MemoryManager implements MemoryManagerInterface
{
  private int reserved_start_addr;
  private int text_start_addr;
  private int data_start_addr;
  private int maximum_addr;
  private PrintStream verbose;

  private TreeMap reserved_map = new TreeMap();
  private TreeMap text_map = new TreeMap();
  private TreeMap data_map = new TreeMap();
  
  private YAMSController controller;

  public MemoryManager(int reserved_segment_size,
                       int text_segment_size,
                       int maximum_memory_size,
                       PrintStream verbose,
                       YAMSController c)
  {
  	this.controller = c;
  	this.verbose = verbose;
    if (reserved_segment_size > 0 &&
        text_segment_size > 0 &&
        reserved_segment_size + text_segment_size < maximum_memory_size)
    {
      /* if the memory segments are suitably sized, assign the global
       variables for the memory manager accordingly */

      reserved_start_addr = 0;
      text_start_addr = reserved_segment_size;
      data_start_addr = text_start_addr + text_segment_size;
      maximum_addr = maximum_memory_size;
    }
    else
    {

      /* if there is an error in sizing memory, then set to MIPS defaults */

      reserved_start_addr = 0;
      text_start_addr = 10000;
      data_start_addr = 20000;
      maximum_addr = 100000;
    }
  }





  public boolean setLocation(int addr, int val) 
  {
  	verbose.println("setLocation " + addr + " (" + Integer.toHexString(addr) + "h) to " + val + " (" + Integer.toHexString(val) + "h)");
    TreeMap destination_map = resolveaddr(addr);
    Integer addr_object = new Integer(addr);
    Integer value_object = new Integer(val);
    if (destination_map != null)
    {
      /* if the addr is in bounds (therefore a destination map exists)*/
      if (!destination_map.containsKey(addr_object))
      {
        /* doesnt contain the addr value, so add to map */
        destination_map.put(addr_object, value_object);
      }
      else
      {
        /* does contain addr, so must replace */
        destination_map.remove(addr_object);
        destination_map.put(addr_object, value_object);
      }
      // let the GUI know about the change
   	  controller.memoryChanged(addr);
      return true;
    }
    else
    {
      /* the addr is not in bounds & no destination map exists */
      //throw new MemoryOutOfBoundsException();
      return false;
    }
  }





  public int getLocation(int addr) 
  {
//	verbose.print("getLocation " + addr + " (" + Integer.toHexString(addr) + "h) returns ");
    
    TreeMap destination_map = resolveaddr(addr);
    Integer addr_object = new Integer(addr);
  if (destination_map != null)
    {
      /* if the addr is in bounds (therefore a destination map exists)*/
      if (destination_map.containsKey(addr_object))
      {
        /* if the addr has something stored at it within the memory map,
         then return the value */
         int val = ((Integer) destination_map.get(addr_object)).intValue();
//		verbose.println(val + " (" + Integer.toHexString(val) + "h)");
        return val;
      }
      else
      {
        /* if the addr has no associated value then return 0 */
//		verbose.println("0 (NO ASSOCIATED VALUE)");
        return 0;
      }
    }
  else
   {
      /* the addr is not in bounds & no destination map exists */
	verbose.println("-1 (NOT IN BOUNDS OR NO DEST MAP)");
      return -1;

    }
  }


	public boolean setByte(int addr, int val) {
		verbose.println("setByte " + addr + " (" + Integer.toHexString(addr) + "h) to " + val + " (" + Integer.toHexString(val) + "h)");
    	int wordaddress = 4 * ((int)(addr / 4));
		int rem = addr % 4;
		// get the word that we're going to write the byte in
		int source = getLocation(wordaddress);
		if(source == -1) {
			// not a valid address
			return false;
		}
		MIPSByte thebyte = new MIPSByte(val);
		MIPSWord theword = new MIPSWord(source);
		int lowbit = 0;
		switch (rem) {
			case 0 : lowbit = 24;
					break;
			case 1 : lowbit = 16;
					break;
			case 2 : lowbit = 8;
					break;
			case 3 : lowbit = 0;
					break;
		}	
		// write the byte to the word
		for(int i = 0; i < 8; i++) {
			theword.setBit(lowbit + i, thebyte.getBit(i));
		}
		// write the word back to memory
		return setLocation(wordaddress, theword.get());
	}


	public int getByte(int addr) {
//		verbose.print("getByte " + addr + " (" + Integer.toHexString(addr) + "h) returns");
    
		int wordaddress = 4 * ((int)(addr / 4));
		int rem = addr % 4;
		// get the word that this byte is in
		int source = getLocation(wordaddress);
		if(source == -1) {
			// not a valid address
			return -1;
		}
		int result = 0;
		MIPSWord theword = new MIPSWord(source);
		// extract the byte from the word
		switch (rem) {
			case 0 : result = MIPSBitstring.extract(theword, 24, 31).toInt();
			break;
			case 1 : result = MIPSBitstring.extract(theword, 16, 23).toInt();
			break;
			case 2 : result = MIPSBitstring.extract(theword, 8, 15).toInt();
			break;
			case 3 : result = MIPSBitstring.extract(theword, 0, 7).toInt();
			break;
		}
//		verbose.println(result + " (" + Integer.toHexString(result) + "h)");
		return result;
	}

 public void reset()
 {
	reserved_map = new TreeMap();
	text_map = new TreeMap();
	data_map = new TreeMap();
 }

/**
 * Return to corresponding treemap for the incoming address
 * @param addr Memory address requested
 * @return
 */
  private TreeMap resolveaddr(int addr) 
  {
    if (addr >= reserved_start_addr && addr < text_start_addr)
    {
      /* if the addr is within the reserved section of memory */
      return reserved_map;
    }
    else if (addr >= text_start_addr && addr < data_start_addr)
    {
      /* if the addr is within the text segment of memory */
      return text_map;
    }
    else if (addr >= data_start_addr && addr <= maximum_addr)
    {
      /* if the addr is within the data/dynamic segment of memory*/
      return data_map;
    }
    else
    {
      /* addr is out of bounds and not contained within memory */
      return null;
    }
  }
  
  public int memoryUsed() {
  	return reserved_map.size() + text_map.size() + data_map.size();
  }
  
  public void setVerbose(PrintStream verbose) {
  	this.verbose = verbose;
  }
  
}
