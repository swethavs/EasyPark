/***************************************************************
Copyright (c) 2015 swethavs

[This program is licensed under the "MIT License"]

Please see the file COPYING in the source

distribution of this software for license terms.
****************************************************************/

package com.swetha.easypark;

import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Location;
import android.util.Log;

public class GetParkingLotsInfoFromDBAdapter {
	static final String DATABASE_NAME = "easypark.db";
	static final int DATABASE_VERSION = 1;
	public static final int NAME_COLUMN = 1;
	public static final double radius = 5.0;
	//public static final int insideradius = (int) (-0.01* 1E6);
	private int current_latitude = 0;
	private int current_longitude = 0;
	// TODO: Create public field for each column in your table.
	// SQL Statement to create a new database.
	static final String PARKINGLOTS_TABLE_CREATE = "create table "+"PARKINGLOTS"+
	                             "( " +"ID"+" integer primary key autoincrement,"+ "LATITUDE  double,LONGITUDE double,ADDRESS text); ";
	// Variable to hold the database instance
	public  SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DataBaseHelper dbHelper;
	public  GetParkingLotsInfoFromDBAdapter(Context _context) 
	{
		Log.i("GetParkingLotsInfoFromDBAdapter","Start of Constructor");
		context = _context;
		dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.i("GetParkingLotsInfoFromDBAdapter","After calling new DataBaseHelper");
		
	}
	public  GetParkingLotsInfoFromDBAdapter open() throws SQLException 
	{
		db = dbHelper.getWritableDatabase();
				return this;
	}
	public void close() 
	{
		db.close();
	}

	public  SQLiteDatabase getDatabaseInstance()
	{
		return db;
	}
	
	public void insertParkingLotsInfo(double latitude, double longitude)
	{
		ContentValues newValues = new ContentValues();
		// Assign values for each row.
		double tableLat = 45.5235694;
		double tableLong = -122.8890;
		newValues.put("LATITUDE", (double)tableLat);
		newValues.put("LONGITUDE",tableLong);
		newValues.put("ADDRESS", "WASHINGTON SQUARE PARK");
		Log.i("GetParkingLotsInfoFromDBAdapter","BeforeInsertion");
		// Insert the row into your table
		db.insert("PARKINGLOTS", null, newValues);
		Log.i("GetParkingLotsInfoFromDBAdapter","AfterInsertion values latitude:" + tableLat + "Longitude:" +  tableLong);

		
	}
	public String getParkingLots(double current_latitude, double current_longitude)
	{
		
		
		Log.i("GetParkingLotsInfoFromDBAdapter","Inside getParkingLots");
		Log.i("GetParkingLotsInfoFromDBAdapter", "vaue of current_latitude{0}"+ current_latitude );
		//String[] req_column = new String[] {"ADDRESS"};
		String whereclause = "LATITUDE <= ? AND LATITUDE >=? AND LONGITUDE <=? AND LONGITUDE >=? ";
		//String current_latitudeplus = Integer.toString(current_latitude + radius);
		String[] whereArgs = new String[] {Double.toString(current_latitude + radius),
										   Double.toString(current_latitude - radius),
										   Double.toString(current_longitude + radius  ),
										   Double.toString(current_longitude - radius)};
		Log.i("GetParkingLotsInfoFromDBAdapter","value of  radius is:\n"+radius);
		Log.i("GetParkingLotsInfoFromDBAdapter","value of Integer.toString(current_latitude in double is:\n"+(current_latitude+ radius));
		Log.i("GetParkingLotsInfoFromDBAdapter","value of Integer.toString(current_latitude in string is:\n"+Double.toString(current_latitude + radius));
		Log.i("GetParkingLotsInfoFromDBAdapter","value of Integer.toString(current_latitude + radius is:\n"+Double.toString(current_latitude + radius));
		Cursor cursortest = db.query("PARKINGLOTS", null, null, null, null, null, null);
		if (cursortest.getCount()<1) // UserName Not Exist
        {
        	cursortest.close();
        	Log.i("GetParkingLotsInfoFromDBAdapter","No Info in PARKINGLOTS table");
        	return "No info in ParkingLots table";
        }
		Log.i("GetParkingLotsInfoFromDBAdapter", "before executing query whereclause:" + whereclause + "whereargs:" +whereArgs );
		Cursor cursor = db.query("PARKINGLOTS", null, whereclause, whereArgs, null, null, null);
		Log.i("GetParkingLotsInfoFromDBAdapter", "After executing query whereclause:" + whereclause + "whereargs:" +whereArgs );
		/*	String sql = "SELECT ADDRESS FROM  PARKINGLOTS WHERE"
					+ "LATITUD_COLUMN > (" + (current_latitude + radius) + ") AND "
					+ "LATITUD_COLUMN < (" + (current_latitude - radius) +") AND "
					+ "LONGITUD_COLUMN > ("+ (current_longitude + radius) +") AND "
					+ "LONGITUD_COLUMN < ("+ (current_longitude - radius) +")";
			
		String sql2 = "LATITUD_COLUMN > (" + (current_latitude + radius) + ") AND "
				+ "LATITUD_COLUMN < (" + (current_latitude - radius) +") AND "
				+ "LONGITUD_COLUMN > ("+ (current_longitude + radius) +") AND "
				+ "LONGITUD_COLUMN < ("+ (current_longitude - radius) +")";*/
		
				if (cursor.getCount()<1) // UserName Not Exist
        {
        	cursor.close();
        	return "NOT EXIST";
        }
	    cursor.moveToFirst();
		String address= cursor.getString(cursor.getColumnIndex("ADDRESS"));
		cursor.close();
		return address;				
	}
	}
