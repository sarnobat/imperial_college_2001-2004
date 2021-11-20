/*
 * Created on 04-Nov-2003
 *
 */

package yams.processor;
import yams.*;
import java.util.TreeMap;

public class RegisterManager implements RegisterManagerInterface {

	private TreeMap regMap = new TreeMap();
	private StatisticsManagerInterface statisticsManager;
	private Integer zero_value = new Integer(0);

	private YAMSController controller;
	
	private int registerTotal = 32;

	public RegisterManager(StatisticsManagerInterface s, YAMSController c) {
		this.controller = c;
		this.statisticsManager = s;

		//initialise the registers in the treemap
		String regname;
		for (int i = 0; i < 32; i++) {
			regname = "$" + i;
			regMap.put(regname, zero_value);
		}
		regMap.put("PC", zero_value);
		regMap.put("LO", zero_value);
		regMap.put("HI", zero_value);
	}

	public boolean setReg(String regid, int val) {
		Integer value_object = new Integer(val);
		if (regMap.containsKey(regid)) {
			// writes to register $0 are discarded
			if (regid.compareTo("$0") == 0) {
				return true;
			}
			regMap.remove(regid);
			regMap.put(regid, value_object);
			//update stats manager
			statisticsManager.regUsed(regid);
			if (regid.compareTo("PC") != 0) {
				//System.out.println("Wrote " + val + " (" + Integer.toHexString(val) + "h)  to register " + regid);
			}
			// tell the controller something has changed
			controller.regChanged(regid);
			return true;
		} else {
			return false; 
		}
	}

	public boolean setReg(int regid, int val) {
		return setReg("$" + regid, val);
	}

	public int getReg(String regid)
	{
		if (regMap.containsKey(regid)) {
			statisticsManager.regUsed(regid);
			int val = ((Integer) regMap.get(regid)).intValue();
			if (regid.compareTo("PC") != 0) {
				//System.out.println("Read " + val + " (" + Integer.toHexString(val) + "h)  from register " + regid);
			}
			return val;
		} else {
			return -1; 
		}
	}

	public int getReg(int regid) {
		return getReg("$" + regid);
	}

	public int getRegOnly(String regid) 
	{
		if (regMap.containsKey(regid)) {
			return ((Integer) regMap.get(regid)).intValue();
		} else {
			return -1; 
		}
	}

	public int getRegOnly(int regid) {
		return getRegOnly("$" + regid);
	}

	public void reset() {
		regMap = new TreeMap();
		String regname;
		for (int i = 0; i < 32; i++) {
			regname = "$" + i;
			regMap.put(regname, zero_value);
		}
		regMap.put("PC", zero_value);
		regMap.put("LO", zero_value);
		regMap.put("HI", zero_value);
	}

	public String getRegName(int id) throws Exception {
		switch (id) {
			case 0 :	return "$zero";
			case 1 :	return "$at";
			case 2 :	return "$v0";
			case 3 :	return "$v1";
			case 4 :	return "$a0";
			case 5 :	return "$a1";
			case 6 :	return "$a2";
			case 7 :	return "$a3";
			case 8 :	return "$t0";
			case 9 :	return "$t1";
			case 10 :	return "$t2";
			case 11 :	return "$t3";
			case 12 :	return "$t4";
			case 13 :	return "$t5";
			case 14 :	return "$t6";
			case 15 :	return "$t7";
			case 16 :	return "$s0";
			case 17 :	return "$s1";
			case 18 :	return "$s2";
			case 19 :	return "$s3";
			case 20 :	return "$s4";
			case 21 :	return "$s5";
			case 22 :	return "$s6";
			case 23 :	return "$s7";
			case 24 :	return "$t8";
			case 25 :	return "$t9";
			case 26 :	return "$k0";
			case 27 :	return "$k1";
			case 28 :	return "$gp";
			case 29 :	return "$sp";
			case 30 :	return "$fp";
			case 31 :	return "$ra";
			default :	throw new Exception("Invalid register id. Must be 0-31.");
		}
	}
	public int getTotalNumberOfRegisters(){
		return registerTotal;
	}
}
