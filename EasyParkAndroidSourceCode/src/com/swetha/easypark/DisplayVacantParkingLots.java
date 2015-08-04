/***************************************************************
Copyright (c) 2015 swethavs

[This program is licensed under the "MIT License"]

Please see the file COPYING in the source

distribution of this software for license terms.
****************************************************************/

package com.swetha.easypark;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.app.ProgressDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;





import com.swetha.helpers.Constants;
import com.swetha.helpers.IconGenerator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;





public class DisplayVacantParkingLots extends FragmentActivity {
	
	public static boolean isRadius;
	GoogleMap googleMap;
	String TAG_PARKINGLOTS = "parkinglots";
	String TAG_SUCCESS = "success";
	// To pass the arraylist to switch view 
	public static final String ARRAYLISTMAP = "com.swetha.easypark.DisplayVacantParkingLots.ARRAYLISTMAP";

	public  ArrayList<HashMap<String, String>> parkingLotsMapList = new ArrayList<HashMap<String, String>>();
	
	TextView AvailableLotAddress;
	
    private ProgressDialog pDialog;
    
    Button btn_switchToListView;

	public static long fromTime;
	public static long toTime;
	 int success;
	 String returnString;
	static final String getParkingLotsurl =  Constants.IPAddress +"/getparkinglotsdata.php";
	//static final String getParkingLotsurl = "http://easypark.net46.net/easypark/getparkinglotsdata.php";
	// static final String getParkingLotsurl = "http://192.168.1.25:80/easypark/getparkinglotsdata.php";
	 CustomHttpClient httpClientObj = new CustomHttpClient();
		public   double radius;
		public long zipcode;
		ListAdapter adapter;
		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapviewlistvacantparkinglots);
		// getIntent() is a method from the started activity
		Intent displayIntent = getIntent(); // gets the previously created intent
		double defaultValue = 0.0;
		double usersCurrentLatitude = displayIntent.getDoubleExtra(GetParkingLots.LATITUDE,defaultValue); 
		double usersCurrentLongitude = displayIntent.getDoubleExtra(GetParkingLots.LONGITUDE, defaultValue);
		 fromTime = displayIntent.getLongExtra(GetParkingLots.FROMTIME, 0);
		 toTime = displayIntent.getLongExtra(GetParkingLots.TOTIME, 0);
		 
		 isRadius = displayIntent.getBooleanExtra(GetParkingLots.RadiusOrZIPCODE, false);
		 if (isRadius)
		 {
		 
		 radius = displayIntent.getDoubleExtra(GetParkingLots.RADIUS, 0.0);
		 }
		 else
		 {
			 
			 zipcode = displayIntent.getLongExtra(GetParkingLots.ZIPCODE, 0);
		 }
		 
	    Log.i("DisplayVacantParkingLots","Before Calling Async task");
		new GetParkingLotsFromWebService(this, usersCurrentLatitude, usersCurrentLongitude, fromTime, toTime).execute();
		Log.i("DisplayVacantParkingLots","After Calling Async task");
		btn_switchToListView = (Button) findViewById(R.id.switchtolistview);
		btn_switchToListView.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					Intent intent = new Intent(DisplayVacantParkingLots.this, DisplayParkingLotsAsList.class);
					intent.putExtra(ARRAYLISTMAP, parkingLotsMapList);
					startActivity(intent);
				}
				});
		
	}

	
	
	@SuppressWarnings("deprecation")
	public void updatemap(ArrayList<HashMap<String, String>> alofHashmap , int Success)
	{
	
		if (success == 1)
		{
			try
			{
		 googleMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.parkinglotsmap)).getMap();
		 IconGenerator ig = new IconGenerator(this);
		  
		  googleMap.clear();
	        for (int i = 0; i< alofHashmap.size(); i++)
	        {
	        	 //canvas.drawText(alofHashmap.get(i).get("costForParking"), 0, 40, paint);
	        LatLng latLng = new LatLng(Double.parseDouble(alofHashmap.get(i).get("latitude")), Double.parseDouble(alofHashmap.get(i).get("longitude")));
	        Bitmap bmp = ig.makeIcon(alofHashmap.get(i).get("costForParking"));
	        googleMap.addMarker(new MarkerOptions()
			.position(latLng)
			//.title()
			//.snippet(alofHashmap.get(i).get("lotsInfoTextView"))
			.icon(BitmapDescriptorFactory.fromBitmap(bmp))).showInfoWindow();
		   	//.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car))).showInfoWindow();
	        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
	        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
	        }
	        
	        googleMap.setOnMarkerClickListener(new OnMarkerClickListener()
            {

                @Override
                public boolean onMarkerClick(Marker arg0) {
                    LatLng markerLatLng = arg0.getPosition();
                    Log.i("DisplayVacantParkingLots","Value of the marker that was clicked is" +markerLatLng.toString());
                    for (int i =0; i< parkingLotsMapList.size(); i++)
                    {
                    	Log.i("DisplayVacantParkingLots", "value in the parkinglots map inside for loop" +parkingLotsMapList.get(i).toString());
                       if   (parkingLotsMapList.get(i).containsValue(String.valueOf(markerLatLng.latitude)) && parkingLotsMapList.get(i).containsValue(String.valueOf(markerLatLng.longitude)))
                       {
                    	   Intent intent = new Intent (DisplayVacantParkingLots.this, GetIndividualParkingSpotDetails.class);
                    	   intent.putExtra("individualParkingLotId", parkingLotsMapList.get(i).get("vacantParkingLotId"));
                    	   Log.i("DisplayVacantParkingLots","The value of the parkinglot of the marker clicked is" +parkingLotsMapList.get(i).get("vacantParkingLotId"));
                    	   intent.putExtra(GetParkingLots.FROMTIME, DisplayVacantParkingLots.fromTime);
                    	   intent.putExtra(GetParkingLots.TOTIME, DisplayVacantParkingLots.toTime);
                    	   startActivity(intent); 
                       }
                    }
                    return true;
                }

            });  
	        }
			catch (Exception e)
			{
			
			}
			finally
			{
				
			}
		}
		else
		{
			AlertDialog alertDialog = new AlertDialog.Builder(
                    DisplayVacantParkingLots.this).create();

    // Setting Dialog Title
    alertDialog.setTitle("Sorry!");

    // Setting Dialog Message
    alertDialog.setMessage("No parking lots found");

    // Setting Icon to Dialog
   // alertDialog.setIcon(R.drawable.tick);

    // Setting OK Button
    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            // Write your code here to execute after dialog closed
            	Intent intent = new Intent(DisplayVacantParkingLots.this, GetParkingLots.class);
	            startActivity(intent);
            }
    });

    // Showing Alert Message
    alertDialog.show();

		}
	}
	
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		
		}
	
	/**
	 * 
	 * @author Swetha Venkatachari Sundarajan
	 *Make an HTTP request and retreive the data 
	 */
	
	class GetParkingLotsFromWebService extends AsyncTask<String, String, String>
	{
		DisplayVacantParkingLots dvpl;
		String latitude, longitude;
		String fromTime, toTime, radius, zipcode;
		double blockedTimeInHours;
		
		 /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DisplayVacantParkingLots.this);
            pDialog.setMessage("Retreiving ParkingLots..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        
        GetParkingLotsFromWebService(DisplayVacantParkingLots dvpl, double latitude, double longitude, long fromTime, long toTime )
        {
        	
        	this.latitude =  String.valueOf(latitude);
        	this.longitude = String.valueOf(longitude);
        	this.fromTime = String.valueOf(fromTime);
        	this.toTime = String.valueOf(toTime);
        	this.radius = String.valueOf(DisplayVacantParkingLots.this.radius);
        	this.zipcode = String.valueOf(DisplayVacantParkingLots.this.zipcode);
        	this.blockedTimeInHours = ((toTime - fromTime)/3600000.0);
        	Log.i("DisplayVacantParkingLots:GetParkingLotsFromWebService","blockedTimeInHours" +blockedTimeInHours);
        	this.dvpl = dvpl;
        	}

		@Override
		protected String doInBackground(String... params) {
			ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
			postParams.add(new BasicNameValuePair("latitude",latitude ));
			postParams.add(new BasicNameValuePair("longitude", longitude));
			postParams.add(new BasicNameValuePair("isradius", String.valueOf(isRadius)));
			postParams.add(new BasicNameValuePair("radius", radius));
			postParams.add(new BasicNameValuePair("zipcode", zipcode));
			postParams.add(new BasicNameValuePair("fromtime", fromTime));
			postParams.add(new BasicNameValuePair("endtime", toTime));
            
            String response = null;
            Log.i("DisplayVacantParkingLots:GetParkingLotsFromWebService","the postparams are" + postParams);

            // call executeHttpPost method passing necessary parameters 
            try {
       response = CustomHttpClient.executeHttpPost(getParkingLotsurl, postParams);
       Log.i("DisplayVacantParkingLots:GetParkingLotsFromWebService","after making request jsonobject is" +response);
       // store the result returned by PHP script that runs MySQL query
       //String result = response.toString();  
                
        //parse json data
           try{
        	  String result = response.toString();
                  returnString = "";
                  JSONObject jsonOb = new JSONObject(result);
                  success = jsonOb.getInt(TAG_SUCCESS);
                  if( success == 1)
                  {
             JSONArray jArray =  jsonOb.getJSONArray(TAG_PARKINGLOTS);
             Log.i("DisplayVacantParkingLots:GetParkingLotsFromWebService","after converting to obj" +jsonOb);

                   for(int i=0;i<jArray.length();i++){
                          JSONObject json_data = jArray.getJSONObject(i);
                           Log.i("log_tag","parkinglotsid: "+json_data.getInt("parkinglotsid")+
                                   ", address: "+json_data.getString("address")+
                                   ", miles: "+json_data.getString("miles")+
                                   ", cost: "+json_data.getString("cost")
                           );
                           //Get an output to the screen
                           HashMap<String, String> parkingLotsMap = new HashMap<String, String>();
               			
               			parkingLotsMap.put("vacantParkingLotId",json_data.getString("parkinglotsid"));
               			parkingLotsMap.put("lotsInfoTextView", json_data.getString("address"));
               			Log.i("DisplayVacantParkingLots:GetParkingLotsFromWebService", "Inside hash map blockedtimeInHours"+blockedTimeInHours);
               			
               			double costForparking = (Double.parseDouble(json_data.getString("cost")) * blockedTimeInHours );
               			Log.i("DisplayVacantParkingLots:GetParkingLotsFromWebService", "Inside hash map costForparking"+costForparking);
               			parkingLotsMap.put("costForParking",(new DecimalFormat("##.##").format(costForparking)+ "$"));
               			
               			double dist = Double.parseDouble(json_data.getString("miles"));
               			parkingLotsMap.put("distanceInMiles",(new DecimalFormat("##.##").format(dist) + "miles"));
               			
               			parkingLotsMap.put("latitude", json_data.getString("latitude"));
               			parkingLotsMap.put("longitude", json_data.getString("longitude"));
               			
               			parkingLotsMapList.add(parkingLotsMap);
            			
                   }
                  }
                  
           }
           catch(JSONException e){
                   Log.e("log_tag", "Error parsing data "+e.toString());
           }
			
		}
            
            catch(Exception e)
            {
                Log.e("log_tag","Error in http connection!!" + e.toString());     

            }
            return null;
		}
		
		  /**
         * After completing background task Dismiss the progress dialog and use the UI thread to update the UI
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            dvpl.updatemap(parkingLotsMapList, success);
 
        }
	}

	
	}
