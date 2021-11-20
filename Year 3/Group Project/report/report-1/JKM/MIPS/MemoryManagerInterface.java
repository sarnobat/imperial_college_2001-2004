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

public interface MemoryManagerInterface
{
  /* stores a value at the supplied address and returns true if the store was
   successful, false if a failure */

  public boolean store(int address, int value);

  /* returns a value at the specified address provided, -1 if the address is
   out of the bounds of memory */

  public int retrieve(int address);
}