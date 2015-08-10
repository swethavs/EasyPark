/***************************************************************
Copyright (c) 2015 swethavs

[This program is licensed under the "MIT License"]

Please see the file COPYING in the source

distribution of this software for license terms.
****************************************************************/

package com.swetha.easypark;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.swetha.easypark.R;
import com.swetha.helpers.Constants;


public class HomeActivity extends Activity 
{
	Button btnSignIn,btnSignUp;
	EasyParkLoginDBAdapter easyParkLoginDBAdapter;
	SharedPreferences sharedpreferences;
	SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
	     super.onCreate(savedInstanceState);
	     setContentView(R.layout.main);
	     sharedpreferences = getSharedPreferences(Constants.MYPREFERENCES, Context.MODE_PRIVATE);
	     editor = sharedpreferences.edit();
         
         if (sharedpreferences.getString(Constants.UNAME, "") != "")
         {
        	 Intent intent = new Intent(HomeActivity.this, GetParkingLots.class);
        	 startActivity(intent);
         }
	      
	     
	     // create a instance of SQLite Database
         easyParkLoginDBAdapter=new EasyParkLoginDBAdapter(this);
         easyParkLoginDBAdapter=easyParkLoginDBAdapter.open();
	     
	     // Get The Reference Of Buttons
	     //btnSignIn=(Button)findViewById(R.id.buttonSignIN);
	     btnSignUp=(Button)findViewById(R.id.buttonSignUP);
			
	    // Set OnClick Listener on SignUp button 
	    btnSignUp.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			/// Create Intent for SignUpActivity  and Start The Activity
			Intent intentSignUP=new Intent(getApplicationContext(),SignUPActivity.class);
			startActivity(intentSignUP);
			}
		});
	}
	// Methods to handleClick Event of Sign In Button
	public  void signIn(View V)
	   {
		
	
		   final  EditText editTextUserName=(EditText)findViewById(R.id.editTextUserNameToLogin);
		    final  EditText editTextPassword=(EditText)findViewById(R.id.editTextPasswordToLogin);
		    
			
			// get The User name and Password
		    String userName=editTextUserName.getText().toString();
					String password=editTextPassword.getText().toString();
					
					editor.putString(Constants.UNAME, userName);
			         editor.commit();
					
					// fetch the Password form database for respective user name
					String storedPassword=easyParkLoginDBAdapter.getSinlgeEntry(userName);
					
					// check if the Stored password matches with  Password entered by user
					if(password.equals(storedPassword))
					{
						
						// When login is successful get the current location of the user and allow user to get the parking lots data
			            Intent intent = new Intent(HomeActivity.this, GetParkingLots.class);
			            startActivity(intent);
						 
					    }
					
					else
					{
						Toast.makeText(HomeActivity.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
					}
				}
			
			
			
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	    // Close The Database
		easyParkLoginDBAdapter.close();
	}
}
