package xtractor;

import java.util.Calendar;
import java.util.GregorianCalendar;

/*
 * Created on 01-J -2004
 *
 */

/**
 * @author ss401
 *
 */
public class Time {
	static Calendar cal = new GregorianCalendar();
	
	/**
	 * 
	 * @return - milliseconds
	 */
	public static String getCurrentTime(){
		return Long.toString(cal.getTimeInMillis());
		
	}

}
