package MIPS;

import java.util.TreeMap;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class MemoryManager
    implements MIPS.MemoryManagerInterface
{

  private int reserved_start_address;
  private int text_start_address;
  private int data_start_address;
  private int maximum_address;

  private TreeMap reserved_map = new TreeMap();
  private TreeMap text_map = new TreeMap();
  private TreeMap data_map = new TreeMap();

  public MemoryManager(int reserved_segment_size,
                       int text_segment_size,
                       int maximum_memory_size)
  {
    if (reserved_segment_size > 0 &&
        text_segment_size > 0 &&
        reserved_segment_size + text_segment_size < maximum_memory_size)
    {
      /* if the memory segments are suitably sized, assign the global
       variables for the memory manager accordingly */

      reserved_start_address = 0;
      text_start_address = reserved_segment_size;
      data_start_address = text_start_address + text_segment_size;
      maximum_address = maximum_memory_size;
    }
    else
    {

      /* if there is an error in sizing memory, then set to MIPS defaults */

      reserved_start_address = 0;
      text_start_address = 10000;
      data_start_address = 20000;
      maximum_address = 100000;
    }
  }

  public boolean store(int address, int value)
  {
    TreeMap destination_map = resolveAddress(address);
    Integer address_object = new Integer(address);
    Integer value_object = new Integer(value);
    if (destination_map != null)
    {
      /* if the address is in bounds (therefore a destination map exists)*/
      if (!destination_map.containsKey(address_object))
      {
        /* doesnt contain the address value, so add to map */
        destination_map.put(address_object, value_object);
      }
      else
      {
        /* does contain address, so must replace */
        destination_map.remove(address_object);
        destination_map.put(address_object, value_object);
      }
      return true;
    }
    else
    {
      /* the address is not in bounds & no destination map exists */
      return false;
    }
  }

  public int retrieve(int address)
  {
    TreeMap destination_map = resolveAddress(address);
    Integer address_object = new Integer(address);
    if (destination_map != null)
    {
      /* if the address is in bounds (therefore a destination map exists)*/
      if (destination_map.containsKey(address_object))
      {
        /* if the address has something stored at it within the memory map,
         then return the value */
        return ( (Integer) destination_map.get(address_object)).intValue();
      }
      else
      {
        /* if the address has no associated value then return 0 */
        return 0;
      }
    }
    else
    {
      /* the address is not in bounds & no destination map exists */
      return -1;
    }
  }

  private TreeMap resolveAddress(int address)
  {
    if (address >= reserved_start_address && address < text_start_address)
    {
      /* if the address is within the reserved section of memory */
      return reserved_map;
    }
    else if (address >= text_start_address && address < data_start_address)
    {
      /* if the address is within the text segment of memory */
      return text_map;
    }
    else if (address >= data_start_address && address <= maximum_address)
    {
      /* if the address is within the data/dynamic segment of memory*/
      return data_map;
    }
    else
    {
      /* address is out of bounds and not contained within memory */
      return null;
    }
  }
}