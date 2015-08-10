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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

import com.swetha.easypark.R;
import com.swetha.helpers.Constants;
import com.swetha.helpers.DateTimeHelpers;



import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



/**
 * This class allows the user to set the duration of parking and get the parking lots data
 * It also has a menu in it which allows the user to view or insert the free parkinglot data
 */

public class GetParkingLots extends FragmentActivity implements LocationListener, DateWatcher
{
	private static String TAG = "getNearByParkingLotsButton";
	GoogleMap googleMap;
	Button btnGetParkingLots;
	Button btnFromTime;
	Button btnToTime;
	LocationManager locationManager ;
	String provider;
	String radius ;
	String zipcode;
	TextView tvaddress;
	EditText et_search;
	RadioGroup rg;
	RadioButton rbRadius;
	RadioButton rbZipCode;
	int checkedRbId;
    boolean gps_enabled, network_enabled;
	
	public static final String LATITUDE = "com.swetha.easypark.GetParkingLots.Location";
	public static final String LONGITUDE = "com.swetha.easypark.GetParkingLots.Longitude";
	public static final String FROMTIME = "com.swetha.easypark.GetParkingLots.FromTime";
	public static final String TOTIME = "com.swetha.easypark.GetParkingLots.TOTime";
	public static final String RADIUS = "com.swetha.easypark.GetParkingLots.Radius";
	public static final String ZIPCODE = "com.swetha.easypark.GetParkingLots.Zipcode";
	public static final String RadiusOrZIPCODE = "com.swetha.easypark.GetParkingLots.RadiusOrZIPCODE";
	public static  boolean isRadiusIndicator;
	
	
	public  Location location = null;
	TextView tv_fromTime;
	TextView tv_toTime;
	
	 DateTimePicker mDateTimePicker;
	 
	 Date dateTimeTo;
	 Date dateTimeFrom;
	 
	 static  String fromTimeString = DateTimeHelpers.dtf.format(new Date()).toString() ;
	 static String toTimeString = DateTimeHelpers.dtf.format(new Date()).toString() ;
	 
	
	 
	 public static double latitude;
	 public static double longitude;

	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		
		
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getparkinglots);
		
		  SupportMapFragment supportMapFragment =
	                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
	        googleMap = supportMapFragment.getMap();
	        
	        googleMap.setMyLocationEnabled(true);
	        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	        network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	       

            	if (gps_enabled)
            	{
	        Criteria criteria = new Criteria();
	        
	    
		   
		   
	        provider = locationManager.getBestProvider(criteria, true);
	        
	                
	        if(provider!=null && !provider.equals("")){
	        	
	        	locationManager.requestLocationUpdates(provider, 500, 1, GetParkingLots.this);
	        	// Get the location from the given provider 
	             location = locationManager.getLastKnownLocation(provider);
	                    
	            
	        }
            	}
            	Log.i("GetParkingLots", "Value of network_enabled and location" +network_enabled +location );
	        if (location == null && network_enabled )
	        {
	        	
	        	location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	        	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 1, GetParkingLots.this);
	        	 
	        }
	    
	            if (location == null && !network_enabled && !gps_enabled)
	            {
	            	Toast.makeText(getBaseContext(), "Enable your location services", Toast.LENGTH_LONG).show();
	            }
	            if(location!=null)
	            	onLocationChanged(location);
	            else
	            {
	            	
	            	Toast.makeText(getBaseContext(), "Location can't be retrieved", Toast.LENGTH_SHORT).show();
	            }
	        
	       
	         tv_fromTime = (TextView) findViewById(R.id.tv_fromTime);
	        fromTimeString = Constants.dtf.format(new Date()).toString();
	        tv_fromTime.setText(fromTimeString);
	        long lval  =  DateTimeHelpers.convertToLongFromTime(Constants.dtf.format(new Date()).toString());
	        Log.i("GetParkingLots","The value of current time:" + Constants.dtf.format(new Date()).toString() + "in long is" + lval);
	        Log.i("GetParkingLots","The value of current time:" + lval + "in long is" + DateTimeHelpers.convertToTimeFromLong(lval));
	        tv_toTime = (TextView) findViewById(R.id.tv_ToTime);
	     // Parsing the date
	        toTimeString = Constants.dtf.format(new Date()).toString();
	        tv_toTime.setText(toTimeString);
	        
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
						Calendar cal;
						@SuppressWarnings("deprecation")
						public void onClick(View v) {
							mDateTimePicker.clearFocus();
							try
							{
							 cal = new GregorianCalendar(mDateTimePicker.getYear(), Integer.parseInt(mDateTimePicker.getMonth()), mDateTimePicker.getDay(), mDateTimePicker.getHour(), mDateTimePicker.getMinute());
							 fromTimeString = DateTimeHelpers.dtf.format(cal.getTime());
							tv_fromTime.setText(fromTimeString);
							}
							catch (Exception e)
							{
								final AlertDialog alertDialog = new AlertDialog.Builder(
				                        GetParkingLots.this).create();
				 
				        
				 
				        
				        alertDialog.setMessage("Enter a valid date");
				 
				      
				        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				                public void onClick(DialogInterface dialog, int which) {
				                
				                	alertDialog.dismiss();
				                }
				        });
				 

				        alertDialog.show();
							}
							
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
						Calendar cal;
						@SuppressWarnings("deprecation")
						public void onClick(View v) {
							mDateTimePicker.clearFocus();
							
							Log.i("toButton","Value of ToString before cal" +toTimeString);
							try
							{
							 cal = new GregorianCalendar(mDateTimePicker.getYear(), Integer.parseInt(mDateTimePicker.getMonth()), mDateTimePicker.getDay(), mDateTimePicker.getHour(), mDateTimePicker.getMinute());
							 toTimeString = DateTimeHelpers.dtf.format(cal.getTime());
								Log.i("toButton","Value of ToString before cal" +toTimeString);
								
								tv_toTime.setText(toTimeString);
							 
							}
							 catch (Exception e) // fixing the bug where the user doesnt enter anything in the textbox
								{
									final AlertDialog alertDialog = new AlertDialog.Builder(
					                        GetParkingLots.this).create();
					 
					        
					 
					        alertDialog.setMessage("Enter a valid date");
					        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
					                public void onClick(DialogInterface dialog, int which) {
					                	alertDialog.dismiss();
					                }
					        });
					 
					        alertDialog.show();
								}
							//dateTimeTo = new DateTime(mDateTimePicker.getYear(), Integer.parseInt(mDateTimePicker.getMonth()) ,  mDateTimePicker.getDay(),  mDateTimePicker.getHour(),  mDateTimePicker.getMinute()); ;
							
							mDateTimeDialog.dismiss();
						}
					});

					
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
			
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				 et_search = (EditText) findViewById(R.id.edittextsearch);
				    rg = (RadioGroup) findViewById(R.id.rg);
				    checkedRbId = rg.getCheckedRadioButtonId();
				    Log.i("LOG_TAG: GetParkingLots", "checked radiobutton id is" +checkedRbId);
				
				if (checkedRbId == R.id.rbradius)
				{
					isRadiusIndicator = true;
					radius = et_search.getText().toString();
				}
				else
				{
					isRadiusIndicator = false;
					zipcode = et_search.getText().toString();
				}
				
				
				final Intent intent = new Intent(GetParkingLots.this, DisplayVacantParkingLots.class);
				Log.i(TAG,"Inside getNearByParkingLots");
				Log.i(TAG,"Value of fromString" +fromTimeString);
				long lFromVal  =  DateTimeHelpers.convertToLongFromTime(fromTimeString);
				Log.i(TAG,"Value of ToString" +toTimeString);
				long lToVal  =  DateTimeHelpers.convertToLongFromTime(toTimeString);
				if ((lToVal - lFromVal) < Constants.thrityMinInMilliSeconds) 
				{
					final AlertDialog alertDialog = new AlertDialog.Builder(
	                        GetParkingLots.this).create();
	 
	        
	 
	        
	        alertDialog.setMessage("You have to park the car for at least 30 min");
	 
	 
	        
	        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                
	                	alertDialog.dismiss();
	                }
	        });
	 
	        
	        alertDialog.show();
	 

				}
				else
				{
				intent.putExtra(LATITUDE, latitude);
				intent.putExtra(LONGITUDE, longitude);
				intent.putExtra(FROMTIME, lFromVal);
				intent.putExtra(TOTIME, lToVal);
				intent.putExtra(RadiusOrZIPCODE, isRadiusIndicator);
				if (isRadiusIndicator)
					try
				{
				
				intent.putExtra(RADIUS, Double.parseDouble(radius));
				startActivity(intent);
				}
				catch(Exception e)
				{
					final AlertDialog alertDialog = new AlertDialog.Builder(
	                        GetParkingLots.this).create();
	 
	        
	 
	        alertDialog.setMessage("Enter valid radius");
	     
	        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                
	                	alertDialog.dismiss();
	                	
	                }
	        });
	 
	        alertDialog.show();
				}
				else 
				{
					try
				{
				
				intent.putExtra(ZIPCODE, Long.parseLong(zipcode));
				startActivity(intent);
				}
				catch(Exception e)
				{
					final AlertDialog alertDialog = new AlertDialog.Builder(
	                        GetParkingLots.this).create();
	 
	        
	 
	        alertDialog.setMessage("Enter a valid Zip code");
	 
	        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                	alertDialog.dismiss();
	                	
	                }
	        });
	 
	        alertDialog.show();
				}
				}
				
				}
				
			}
			});
		
	}
	
	
	@Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.freeparkingmenu:
            	Intent intent = new Intent(GetParkingLots.this, FreeParkingLotsNearMeActivity.class);
            	//intent.putExtra(LATITUDE, GetParkingLots.latitude);
            	//intent.putExtra(LONGITUDE, GetParkingLots.longitude);
            	 startActivity(intent);
            	break;
            	
            	 }
        return true;
    }
	
	@SuppressWarnings("static-access")
	@Override
	public void onLocationChanged(Location location) {
		if (location == null)
		{
			Toast.makeText(getApplicationContext(), "Please Enble your location services",Toast.LENGTH_LONG).show();
		}
		try
		{
		Log.i("GetParkingLots","Inside OnLocationChanged");
		 latitude = location.getLatitude();
		Log.i("GetParkingLots","After Latitude");
		 longitude = location.getLongitude();
		Log.i("GetParkingLots","After Textviewtv_longitude");
		// no concept of static class in Java that is not nested?
				AddressFromLatLng locationAddress = new AddressFromLatLng(); 
                locationAddress.getAddressFromLocation(latitude, longitude,
                        getApplicationContext(), new GeocoderHandler());
				}
		catch(Exception e)
		{
			
		}
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
		
		private static final String TAG = "GetParkingLots";
        @Override
        public void handleMessage(Message message) {
        	try
        	{
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
         
          
        	Log.i(TAG,"Before Setting google map");
			LatLng latLng = new LatLng(latitude, longitude);
			googleMap.clear();
	        googleMap.addMarker(new MarkerOptions()
	        							.position(latLng)
	        							.title(locationAddress)
	        						   	.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car))).showInfoWindow();
	        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
	        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
	        Log.i(TAG,"After  setting Google map");
        	}
        	catch(Exception e)
        	{
        		
        	}
            
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
		
		
	        

	
