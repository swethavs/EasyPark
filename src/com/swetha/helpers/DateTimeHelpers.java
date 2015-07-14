/***************************************************************
Copyright (c) 2015 swethavs

[This program is licensed under the "MIT License"]

Please see the file COPYING in the source

distribution of this software for license terms.
****************************************************************/


package com.swetha.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public  class DateTimeHelpers {
	 public static final SimpleDateFormat dtf = new SimpleDateFormat("MM-dd-yyyy HH:mm");

	public static String convertToTimeFromLong(long time){
	    Calendar cal = Calendar.getInstance();
	    //cal.setTimeZone(TimeZone.getTimeZone("PDT"));
	    cal.setTimeInMillis(time);
	    return ((cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" 
	            + cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.HOUR_OF_DAY) + ":"
	            + cal.get(Calendar.MINUTE));

	}
	
	public static long convertToLongFromTime(String string_time){
		Date d = new Date();
		try
		{
			
		d = dtf.parse(string_time);
		}
		catch(Exception e)
		{
			e.setStackTrace(null);
		}
		return d.getTime();

	}

}
