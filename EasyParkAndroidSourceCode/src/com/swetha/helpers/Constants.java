package com.swetha.helpers;

import java.text.SimpleDateFormat;

public class Constants {
	
	
	//public static String IPAddress = "http://192.168.1.25:80/easypark";  //Home
	//public static String IPAddress = "http://172.30.15.107:80/easypark";	//PSU
	public static String IPAddress = "http://easypark.net46.net/easypark";  // Site hosted in 000webhost.com 
	public static String IPAddressForFreePArkingLots = "http://192.168.1.25:80/easypark"; 
	
	
	public static double millisecondsIntoHours = 3600000.0;
	public static double doubleDefaultValue = 0.0;
	public static long thrityMinInMilliSeconds = 1800000;// milliseconds to 30 min
	public static long tenMinutesInMilliseconds = 600000;
	
	public static String TAG_SUCCESS = "success";
	public static String hmFreePLlatitude = "latitude";
	public static String hmFreePLlongitude = "longitude";
	public static String hmFreePLAddress = "address";
	public static String hmFreePLNoOfSpots = "nooflots";
	public static String hmFreePLMiles = "miles";
	public static String hmFreePLTime = "time";
	public static String hmFreePLUser = "user";
	
	public static String GOOGLEDIRECTIONSURL = "http://maps.googleapis.com/maps/api/directions/xml?";
	public final static String MODE_DRIVING = "driving";
	public final static String MYPREFERENCES = "MYPREFERENCES";
	public final static String UNAME = "UNAME";
	public final static String UPDATEDBY = "Updated by";
	public final static String AT= "at";
	public final static String VACANTSPOTS = "Vacant spot(s) =";
		
	
	
	 public final static  SimpleDateFormat dtf = new SimpleDateFormat("MM-dd-yyyy HH:mm");

}
