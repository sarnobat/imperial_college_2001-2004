/*
 * Created on 21-Nov-2003
 *
 */
package yams.processor;
import java.util.HashSet;
import java.util.Set;
import yams.exceptions.YAMSRuntimeException;

/**
 * Provides management of breakpoints.
 * 
 * @author sw00
 */
public class BreakpointManager {

	private Set bpSet;
	
	/**
	 * Creates a new breakpoint manager.
	 */
	public BreakpointManager() {
		bpSet = new HashSet();
	}

	/**
	 * Returns whether there is a breakpoint at an address
	 * @param address
	 * @return true if a breakpoint at address
	 */
	public boolean isBreakpoint(int address) {
		return bpSet.contains(new Integer(address));
	}
	
	/**
	 * Sets a breakpoint
	 * @param address
	 */
	public void setBreakpoint(int address) {
		Integer intaddr = new Integer(address);
		if(!bpSet.contains(intaddr)) {
			bpSet.add(intaddr);
		}
	}
	
	/**
	 * Clears a breakpoint
	 * @param address
	 */
	public void clearBreakpoint(int address) {
		Integer intaddr = new Integer(address);
		if(!bpSet.contains(intaddr)) {
			throw new YAMSRuntimeException("Cannot clear non-existent breakpoint");			
		}
		bpSet.remove(intaddr);
	}

}
