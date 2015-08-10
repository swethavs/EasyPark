package com.swetha.easypark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.swetha.helpers.Constants;
import com.swetha.helpers.DateTimeHelpers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class KeyInFreeParkingSpotValues extends FragmentActivity{
	
	GoogleMap googleMap;
	Button insertButton;
	EditText etNoOfFreeSpots;
	boolean gps_enabled, network_enabled;
	double vacantlotLatitude = GetParkingLots.latitude;
	double vacantlotLongitude = GetParkingLots.longitude;
	String user = "";
	String noOfFreeSpots;
	LatLng latLng;
	ProgressDialog  pDialog;
	
	String  insertFreeParkingSpotsurl =  Constants.IPAddress +"/insertfreeparkinglots.php";
	String TAG = "KeyInFreeParkingSpotValues";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insertvacantlot);
		SupportMapFragment supportMapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.freevacantparkinglotsmap);
        googleMap = supportMapFragment.getMap();
        
         latLng = new LatLng(GetParkingLots.latitude, GetParkingLots.longitude);
		googleMap.clear();
        googleMap.addMarker(new MarkerOptions()
        							.position(latLng)
        							.draggable(true)).showInfoWindow();
        							
        						   	
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(18)); 
        
        googleMap.setOnMarkerDragListener(new OnMarkerDragListener() {
			
			
			@Override
			public void onMarkerDragEnd(Marker marker) {
				// TODO Auto-generated method stub
				vacantlotLatitude = marker.getPosition().latitude;
				vacantlotLongitude = marker.getPosition().longitude;
				}

			@Override
			public void onMarkerDrag(Marker marker) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onMarkerDragStart(Marker marker) {
				// TODO Auto-generated method stub
				
			}
        });
        
        insertButton = (Button) findViewById(R.id.insertvacantspotsbutton);
        insertButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				etNoOfFreeSpots = (EditText)findViewById(R.id.edittextnumberoffreevacantspots);
				noOfFreeSpots = etNoOfFreeSpots.getText().toString();
				SharedPreferences shared = getSharedPreferences(Constants.MYPREFERENCES, MODE_PRIVATE);
				user = (shared.getString(Constants.UNAME, ""));
				// TODO Auto-generated method stub
				new StoreFreeVacantParkingSpotsInDb(KeyInFreeParkingSpotValues.this, vacantlotLatitude, vacantlotLongitude, getApplicationContext()).execute();
			}
		});
        
	}
	
	public class StoreFreeVacantParkingSpotsInDb extends AsyncTask<String, String, String>
	{
		KeyInFreeParkingSpotValues kinFpSV;
		String latitude, longitude;
		String time;
		int success;
		Context context;
		
		 /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(KeyInFreeParkingSpotValues.this);
            pDialog.setMessage("Inserting values..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
		public StoreFreeVacantParkingSpotsInDb(KeyInFreeParkingSpotValues kiFPSV, Double laDouble, Double lngDouble, Context ctxt)
		{
			this.kinFpSV = kiFPSV;
			this.latitude = String.valueOf(laDouble);
			this.longitude = String.valueOf(lngDouble);
			this.time = String.valueOf(DateTimeHelpers.convertToLongFromTime(Constants.dtf.format(new Date()).toString()));
			this.context = ctxt;
		}
		

		@Override
		protected String doInBackground(String... params) {
			ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
			postParams.add(new BasicNameValuePair(Constants.hmFreePLlatitude,latitude ));
			postParams.add(new BasicNameValuePair(Constants.hmFreePLlongitude, longitude));
			String address = getAddressFromLocation(vacantlotLatitude, vacantlotLongitude, context);
			postParams.add(new BasicNameValuePair(Constants.hmFreePLAddress, address));
			postParams.add(new BasicNameValuePair(Constants.hmFreePLNoOfSpots,noOfFreeSpots ));
			postParams.add(new BasicNameValuePair(Constants.hmFreePLTime, time));
			postParams.add(new BasicNameValuePair(Constants.hmFreePLUser, user.toString()));
            
            String response = null;
            Log.i(TAG,"the postparams are" + postParams);

            // call executeHttpPost method passing necessary parameters 
            try {
       response = EasyParkHttpClient.executeHttpPost(insertFreeParkingSpotsurl, postParams);
       Log.i(TAG,"after making request jsonobject is" +response);
       // store the result returned by PHP script that runs MySQL query
       //String result = response.toString();  
                
        //parse json data
           try{
        	  String result = response.toString();
                
                  JSONObject jsonOb = new JSONObject(result);
                  success = jsonOb.getInt(Constants.TAG_SUCCESS);
                  if( success == 1)
                  {
                	  
                  }
                  
           }
           catch(JSONException e)
           {
        	   Log.e(TAG, "Error parsing data "+e.toString());
           }
            }
            catch(Exception e)
            {
            	Log.e(TAG, "Error in HTTP Connection" +e.toString());
            }
		
			return null;

	}//doInBackground
		
		private String getAddressFromLocation(double lat, double lng, Context context)
		{
			Geocoder geocoder = new Geocoder(context, Locale.US);
			List<Address> addressList;
			String result = "";
            try {
            	addressList = geocoder.getFromLocation(
                        lat, lng, 1);
                if (addressList != null && addressList.size() > 0) {
                    Address address = addressList.get(0);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i)).append("\n");
                    }
                    sb.append(address.getLocality()).append("\n");
                   // sb.append(address.getPostalCode()).append("\n");
                   
                    result = sb.toString();
                }
            } catch (IOException e) {
                Log.e(TAG, "Unable to connect to Geocoder", e);
            } finally {
                
                if (result != null) {
                    return result;
                } else {
                	
                	// try one more time
                	// TODO: refactor code consider adding GOTO
                	try {
                    	addressList = geocoder.getFromLocation(
                                lat, lng, 1);
                        if (addressList != null && addressList.size() > 0) {
                            Address address = addressList.get(0);
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                sb.append(address.getAddressLine(i)).append(",");
                            }
                           // sb.append(address.getLocality()).append(",");
                           // sb.append(address.getPostalCode()).append("\n");
                           
                            result = sb.toString().replace(" ", "%20");
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Unable to connect to Geocoder 2nd time", e);
                    } 
                	finally 
                	{
                		if (result != null) {
                            return result;
                        } 
                        }
                		
                	}
                	
                    
                }
            return result;
                
            }
		 protected void onPostExecute(String file_url) {
	            
	            pDialog.dismiss();
	            kinFpSV.updateUI(success);
	 
	        }
		
	}//InnerClass

	@SuppressWarnings("deprecation")
	public void updateUI(int success) {
		// TODO Auto-generated method stub
		if (success == 1)
		{
			AlertDialog alertDialog = new AlertDialog.Builder(
                    KeyInFreeParkingSpotValues.this).create();

   
    alertDialog.setMessage("Value was successfully inserted");



   
    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
		
		}
		
	});
    alertDialog.show();
		}
		
		else
		{
			AlertDialog alertDialog = new AlertDialog.Builder(
                    KeyInFreeParkingSpotValues.this).create();

   
    alertDialog.setTitle("Sorry!");

 
    alertDialog.setMessage("There was an error in updating. Try again");



   
    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
		
		}
		
	});
    alertDialog.show();
}
	
	}
}
