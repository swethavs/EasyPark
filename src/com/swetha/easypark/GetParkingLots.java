/***************************************************************
Copyright (c) 2015 swethavs

[This program is licensed under the "MIT License"]

Please see the file COPYING in the source

distribution of this software for license terms.
****************************************************************/

package com.swetha.easypark;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


import com.swetha.datetimepicker.DateTimePicker.DateWatcher;
import com.swetha.datetimepicker.DateTimePicker;

import android.app.Dialog;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

import com.swetha.easypark.R;
import com.swetha.helpers.DateTimeHelpers;



import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;




public class GetParkingLots extends FragmentActivity implements LocationListener, DateWatcher
{
	GoogleMap googleMap;
	Button btnGetParkingLots;
	Button btnFromTime;
	Button btnToTime;
	LocationManager locationManager ;
	String provider;
	String radius;
	TextView tvaddress;
	EditText et_radius;
	public static final String LATITUDE = "com.swetha.easypark.GetParkingLots.Location";
	public static final String LONGITUDE = "com.swetha.easypark.GetParkingLots.Longitude";
	public static final String FROMTIME = "com.swetha.easypark.GetParkingLots.FromTime";
	public static final String TOTIME = "com.swetha.easypark.GetParkingLots.TOTime";
	public static final String RADIUS = "com.swetha.easypark.GetParkingLots.Radius";
	public  Location location = null;
	TextView tv_fromTime;
	TextView tv_toTime;
	
	 DateTimePicker mDateTimePicker;
	 
	 Date dateTimeTo;
	 Date dateTimeFrom;
	 
	 static  String fromTimeString = DateTimeHelpers.dtf.format(new Date()).toString() ;
	 static String toTimeString = DateTimeHelpers.dtf.format(new Date()).toString() ;
	 
	 final SimpleDateFormat dtf = new SimpleDateFormat("MM-dd-yyyy HH:mm");
	 
	 public double latitude;
	 public double longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		  .detectDiskReads().detectDiskWrites().detectNetwork() // StrictMode is most commonly used to catch accidental disk or network access on the application's main thread
		  .penaltyLog().build());
		//show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getparkinglots);
		
		  SupportMapFragment supportMapFragment =
	                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
	        googleMap = supportMapFragment.getMap();
	        googleMap.setMyLocationEnabled(true);
	        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	        Criteria criteria = new Criteria();
	        
	    
		   
		   // Getting the name of the provider that meets the criteria
	        provider = locationManager.getBestProvider(criteria, true);
	        
	                
	        if(provider!=null && !provider.equals("")){
	        	
	        	// Get the location from the given provider 
	             location = locationManager.getLastKnownLocation(provider);
	                        
	            locationManager.requestLocationUpdates(provider, 20000, 1, GetParkingLots.this);
	            
	            
	            if(location!=null)
	            	onLocationChanged(location);
	            else
	            	Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
	            
	        }else{
	        	Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
	        }
	        
	        tv_fromTime = (TextView) findViewById(R.id.tv_fromTime);
	        tv_fromTime.setText(dtf.format(new Date()).toString());
	        long lval  =  DateTimeHelpers.convertToLongFromTime(dtf.format(new Date()).toString());
	        Log.i("GetParkingLots","The value of current time:" + dtf.format(new Date()).toString() + "in long is" + lval);
	        Log.i("GetParkingLots","The value of current time:" + lval + "in long is" + DateTimeHelpers.convertToTimeFromLong(lval));
	        tv_toTime = (TextView) findViewById(R.id.tv_ToTime);
	     // Parsing the date
	    
	        tv_toTime.setText(dtf.format(new Date()).toString());
	        
	        btnFromTime = (Button) findViewById(R.id.fromButton);
	        btnFromTime.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					
					// Create the dialog
			    	final Dialog mDateTimeDialog = new Dialog(GetParkingLots.this);
					// Inflate the root layout
					final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater().inflate(R.layout.date_time_dialog, null);
					// Grab widget instance
					 mDateTimePicker = (DateTimePicker) mDateTimeDialogView.findViewById(R.id.DateTimePicker);
					mDateTimePicker.setDateChangedListener(GetParkingLots.this); 
					 
					// Update demo TextViews when the "OK" button is clicked 
					((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime)).setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							mDateTimePicker.clearFocus();
							
							Calendar cal = new GregorianCalendar(mDateTimePicker.getYear(), Integer.parseInt(mDateTimePicker.getMonth()), mDateTimePicker.getDay(), mDateTimePicker.getHour(), mDateTimePicker.getMinute());
							// TODO Auto-generated method stub 
							//fromTimeString = mDateTimePicker.getMonth() + "-" + String.valueOf(mDateTimePicker.getDay()) + "-" + String.valueOf(mDateTimePicker.getYear())
							//		+ "  " + String.valueOf(mDateTimePicker.getHour()) + ":" + String.valueOf(mDateTimePicker.getMinute());
//							if(mDateTimePicker.getHour() > 12) result_string = result_string + "PM";
//							else result_string = result_string + "AM";
							fromTimeString = DateTimeHelpers.dtf.format(cal.getTime());
							tv_fromTime.setText(fromTimeString);
							mDateTimeDialog.dismiss();
						}
					});

					// Cancel the dialog when the "Cancel" button is clicked
					((Button) mDateTimeDialogView.findViewById(R.id.CancelDialog)).setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							mDateTimeDialog.cancel();
						}
					});

					// Reset Date and Time pickers when the "Reset" button is clicked
				
					((Button) mDateTimeDialogView.findViewById(R.id.ResetDateTime)).setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							mDateTimePicker.reset();
						}
					});
					  
					// Setup TimePicker
					// No title on the dialog window
					mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					// Set the dialog content view
					mDateTimeDialog.setContentView(mDateTimeDialogView);
					// Display the dialog
					mDateTimeDialog.show();				

				}
				});
			
		
	        btnToTime = (Button) findViewById(R.id.toButton);
	        btnToTime.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					
					// Create the dialog
			    	final Dialog mDateTimeDialog = new Dialog(GetParkingLots.this);
					// Inflate the root layout
					final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater().inflate(R.layout.date_time_dialog, null);
					// Grab widget instance
					final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView.findViewById(R.id.DateTimePicker);
					mDateTimePicker.setDateChangedListener(GetParkingLots.this); 
					 
					// Update demo TextViews when the "OK" button is clicked 
					((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime)).setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							mDateTimePicker.clearFocus();
							Log.i("toButton","Value of ToString before cal" +toTimeString);
							Calendar cal = new GregorianCalendar(mDateTimePicker.getYear(), Integer.parseInt(mDateTimePicker.getMonth()), mDateTimePicker.getDay(), mDateTimePicker.getHour(), mDateTimePicker.getMinute());
							
							//dateTimeTo = new DateTime(mDateTimePicker.getYear(), Integer.parseInt(mDateTimePicker.getMonth()) ,  mDateTimePicker.getDay(),  mDateTimePicker.getHour(),  mDateTimePicker.getMinute()); ;
							toTimeString = DateTimeHelpers.dtf.format(cal.getTime());
							Log.i("toButton","Value of ToString before cal" +toTimeString);
//							if(mDateTimePicker.getHour() > 12) result_string = result_string + "PM";
//							else result_string = result_string + "AM";
							tv_toTime.setText(toTimeString);
							mDateTimeDialog.dismiss();
						}
					});

					// Cancel the dialog when the "Cancel" button is clicked
					((Button) mDateTimeDialogView.findViewById(R.id.CancelDialog)).setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							mDateTimeDialog.cancel();
						}
					});

					// Reset Date and Time pickers when the "Reset" button is clicked
				
					((Button) mDateTimeDialogView.findViewById(R.id.ResetDateTime)).setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							// TODO Auto-generated method stub
							mDateTimePicker.reset();
						}
					});
					  
					// Setup TimePicker
					// No title on the dialog window
					mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					// Set the dialog content view
					mDateTimeDialog.setContentView(mDateTimeDialogView);
					// Display the dialog
					mDateTimeDialog.show();				

				}
				});
	        
	        
		btnGetParkingLots=(Button)findViewById(R.id.getNearByParkingLotsButton);
		btnGetParkingLots.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				et_radius = (EditText) findViewById(R.id.edittextradius);
				radius = et_radius.getText().toString();
				Intent intent = new Intent(GetParkingLots.this, DisplayVacantParkingLots.class);
				Log.i("getNearByParkingLotsButton","Inside getNearByParkingLots");
				Log.i("getNearByParkingLotsButton","Value of fromString" +fromTimeString);
				long lFromVal  =  DateTimeHelpers.convertToLongFromTime(fromTimeString);
				Log.i("getNearByParkingLotsButton","Value of ToString" +toTimeString);
				long lToVal  =  DateTimeHelpers.convertToLongFromTime(toTimeString);
				intent.putExtra(LATITUDE, location.getLatitude());
				intent.putExtra(LONGITUDE, location.getLongitude());
				intent.putExtra(FROMTIME, lFromVal);
				intent.putExtra(TOTIME, lToVal);
				intent.putExtra(RADIUS, Double.parseDouble(radius));
	            startActivity(intent);
			}
			});
		
	}
	
	@Override
	public void onLocationChanged(Location location) {
		Log.i("GetParkingLots","Inside OnLocationChanged");
		 latitude = location.getLatitude();
		Log.i("GetParkingLots","After Latitude");
		 longitude = location.getLongitude();
		// Getting reference to TextView tv_longitude
			//TextView tvLongitude = (TextView)findViewById(R.id.tv_longitude);
			Log.i("GetParkingLots","After Textviewtv_longitude");
				// Getting reference to TextView tv_latitude
			//TextView tvLatitude = (TextView)findViewById(R.id.tv_latitude);
				
				// Setting Current Longitude
				//tvLongitude.setText("Longitude:" + longitude);
			
				// Setting Current Latitude
				//tvLatitude.setText("Latitude:" +  latitude);
				LocationAddress locationAddress = new LocationAddress();
                locationAddress.getAddressFromLocation(latitude, longitude,
                        getApplicationContext(), new GeocoderHandler());
				}
	
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	
	private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
         
          
        	Log.i("GetParkingLots","Before Setting google map");
			LatLng latLng = new LatLng(latitude, longitude);
			googleMap.clear();
	        googleMap.addMarker(new MarkerOptions()
	        							.position(latLng)
	        							.title(locationAddress)).showInfoWindow();
	        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
	        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
	        Log.i("GetParkingLots","After  setting Google map");
            
        }
    }
	
	public void onDateChanged(Calendar c) { 
		Log.e("",
				"" + c.get(Calendar.MONTH) + " " + c.get(Calendar.DAY_OF_MONTH)
						+ " " + c.get(Calendar.YEAR));
	}
	
	private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }
	}
		
		
	        

	
