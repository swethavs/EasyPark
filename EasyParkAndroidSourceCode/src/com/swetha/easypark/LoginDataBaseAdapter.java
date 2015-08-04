/***************************************************************
Copyright (c) 2015 swethavs

[This program is licensed under the "MIT License"]

Please see the file COPYING in the source

distribution of this software for license terms.
****************************************************************/

package com.swetha.easypark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LoginDataBaseAdapter 
{
		static final String DATABASE_NAME = "easypark.db";
		static final int DATABASE_VERSION = 1;
		public static final int NAME_COLUMN = 1;
		
		// SQL Statement to create a new table.
		static final String DATABASE_CREATE = "create table "+"LOGIN"+
		                             "( " +"ID"+" integer primary key autoincrement,"+ "USERNAME  text,PASSWORD text); ";
		// Variable to hold the database instance
		public  SQLiteDatabase db;
		// Context of the application using the database.
		private final Context context;
		// Database open/upgrade helper
		private DataBaseHelper dbHelper;
		public  LoginDataBaseAdapter(Context _context) 
		{
			Log.i("LoginDataBaseAdapter","Inside CONSTRUCTOR");
			context = _context;
			dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
			Log.i("LoginDataBaseAdapter","END OF  CONSTRUCTOR");
		}
		public  LoginDataBaseAdapter open() throws SQLException 
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

		public String insertEntry(String userName,String password)
		{
			
			Cursor cursor=db.query("LOGIN", null, " USERNAME=?", new String[]{userName}, null, null, null);
	        if(cursor.getCount()<1) // UserName Not Exist
	        {
	        	cursor.close();
	       ContentValues newValues = new ContentValues();
			// Assign values for each row.
			newValues.put("USERNAME", userName);
			newValues.put("PASSWORD",password);
			
			// Insert the row into your table
			db.insert("LOGIN", null, newValues);
			 return "Account Successfully Created";
	        }
	        else
	        {
	        	return "Enter a different user name";
	        }
	        	
		}
		public int deleteEntry(String UserName)
		{
			//String id=String.valueOf(ID);
		    String where="USERNAME=?";
		    int numberOFEntriesDeleted= db.delete("LOGIN", where, new String[]{UserName}) ;
	       // Toast.makeText(context, "Number of Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
	        return numberOFEntriesDeleted;
		}	
		public String getSinlgeEntry(String userName)
		{
			Cursor cursor=db.query("LOGIN", null, " USERNAME=?", new String[]{userName}, null, null, null);
	        if(cursor.getCount()<1) // UserName Not Exist
	        {
	        	cursor.close();
	        	return "NOT EXIST";
	        }
		    cursor.moveToFirst();
			String password= cursor.getString(cursor.getColumnIndex("PASSWORD"));
			cursor.close();
			return password;				
		}
		public void  updateEntry(String userName,String password)
		{
			
			ContentValues updatedValues = new ContentValues();
			
			updatedValues.put("USERNAME", userName);
			updatedValues.put("PASSWORD",password);
			
	        String where="USERNAME = ?";
		    db.update("LOGIN",updatedValues, where, new String[]{userName});			   
		}		
}

