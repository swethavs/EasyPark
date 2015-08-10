/***************************************************************
Copyright (c) 2015 swethavs

[This program is licensed under the "MIT License"]

Please see the file COPYING in the source

distribution of this software for license terms.

// refered http://www.learn-android-easily.com/2013/03/developing-app-for-user-sign-insign-up.html

****************************************************************/

package com.swetha.easypark;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class DataBaseHelper extends SQLiteOpenHelper
{
	public DataBaseHelper(Context context, String name,CursorFactory factory, int version) 
    {
		
	           super(context, name, factory, version);
	           Log.i("DataBaseHelper","INSIDE CONSTRUCTOR AFTER SUPER");
	}
	
	@Override
	public void onCreate(SQLiteDatabase _db) 
	{
		Log.i("DataBaseHelper","INSIDE ON CREATE");
			_db.execSQL(EasyParkLoginDBAdapter.DATABASE_CREATE);
			Log.i("DataBaseHelper","AFTER ON CREATE");
			
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) 
	{
			// Log the version upgrade.
			Log.w("TaskDBAdapter", "Upgrading from version " +_oldVersion + " to " +_newVersion + ", which will destroy all old data");
	
	
			
			_db.execSQL("DROP TABLE IF EXISTS " + "TEMPLATE");
			// Create a new one.
			onCreate(_db);
	}
	

}
