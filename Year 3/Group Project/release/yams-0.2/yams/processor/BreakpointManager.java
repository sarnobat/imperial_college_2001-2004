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

	public boolean isBreakpoint(int address) {
		return bpSet.contains(new Integer(address));
	}
	
	public void setBreakpoint(int address) {
		Integer intaddr = new Integer(address);
		if(!bpSet.contains(intaddr)) {
			bpSet.add(intaddr);
		}
	}
	
	public void clearBreakpoint(int address) {
		Integer intaddr = new Integer(address);
		if(!bpSet.contains(intaddr)) {
			throw new YAMSRuntimeException("Cannot clear non-existent breakpoint");			
		}
		bpSet.remove(intaddr);
	}

}
