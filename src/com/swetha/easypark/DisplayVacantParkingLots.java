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
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.app.ProgressDialog;
import android.content.Intent;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class DisplayVacantParkingLots extends ListActivity {
	
	String TAG_PARKINGLOTS = "parkinglots";
	String TAG_SUCCESS = "success";
	ArrayList<HashMap<String, String>> parkingLotsMapList = new ArrayList<HashMap<String, String>>();
	//GetParkingLotsInfoFromDBAdapter getParkingLotsFromDB;
	TextView AvailableLotAddress;
	 // Progress Dialog
    private ProgressDialog pDialog;
	TextView vacntParkingLotId;
	 long fromTime;
	 long toTime;
	 int success;
	 String returnString;
	// static final String getParkingLotsurl = "http://172.30.2.111:80/easypark/getparkinglotsdata.php";
	 static final String getParkingLotsurl = "http://192.168.1.25:80/easypark/getparkinglotsdata.php";
	 CustomHttpClient httpClientObj = new CustomHttpClient();
		public   double radius = 100;
		ListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.displayvacantparkinglots);
		// getIntent() is a method from the started activity
		Intent displayIntent = getIntent(); // gets the previously created intent
		double defaultValue = 0.0;
		double usersCurrentLatitude = displayIntent.getDoubleExtra(GetParkingLots.LATITUDE,defaultValue); 
		double usersCurrentLongitude = displayIntent.getDoubleExtra(GetParkingLots.LONGITUDE, defaultValue);
		 fromTime = displayIntent.getLongExtra(GetParkingLots.FROMTIME, 0);
		 toTime = displayIntent.getLongExtra(GetParkingLots.TOTIME, 0);
		 radius = displayIntent.getDoubleExtra(GetParkingLots.RADIUS, 0.0);
		 
		
		/*getParkingLotsFromDB =  new GetParkingLotsInfoFromDBAdapter(this);
		getParkingLotsFromDB = getParkingLotsFromDB.open();
		getParkingLotsFromDB.insertParkingLotsInfo(usersCurrentLatitude, usersCurrentLongitude);
		Log.i("DisplayVacantParkingLots","BeforeCallingDB getParkingLotsFromDB.getParkingLots");
		String availableAddress = getParkingLotsFromDB.getParkingLots(usersCurrentLatitude, usersCurrentLongitude);
		Log.i("DisplayVacantParkingLots","After CallingDB getParkingLotsFromDB.getParkingLots");*/
		
		// Get the ListView and assign an event handler to it
		
					ListView listView = getListView();
					listView.setOnItemClickListener(new OnItemClickListener() {
						
						public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
							
							// When an item is clicked get the TextView
							// with a matching checkId
							Log.i("DisplayVacantParkingLots", "AfterOnItemClick");
							vacntParkingLotId = (TextView) view.findViewById(R.id.vacantParkingLotId);
							
							// Convert that contactId into a String
							
							String vacntParkingLotIdValue = vacntParkingLotId.getText().toString();	
							
							// Signals an intention to do something
							// getApplication() returns the application that owns
							// this activity
							
							Intent  theIndent = new Intent(DisplayVacantParkingLots.this, GetIndividualParkingSpotDetails.class);
							
							// Put additional data in for EditContact to use
							
							theIndent.putExtra("individualParkingLotId", vacntParkingLotIdValue); 
							theIndent.putExtra(GetParkingLots.FROMTIME, fromTime);
							theIndent.putExtra(GetParkingLots.TOTIME, toTime);
							// Calls for EditContact
							
							startActivity(theIndent); 
						}
					}); 
					
					
					Log.i("DisplayVacantParkingLots","Before Calling Async task");
		new GetParkingLotsFromWebService(this, usersCurrentLatitude, usersCurrentLongitude, fromTime, toTime).execute();
		Log.i("DisplayVacantParkingLots","After Calling Async task");
		/*if(availableAddress!=null) {
			
			
			// A list adapter is used bridge between a ListView and
			// the ListViews data
			
			// The SimpleAdapter connects the data in an ArrayList
			// to the XML file
			
			// First we pass in a Context to provide information needed
			// about the application
			// The ArrayList of data is next followed by the xml resource
			// Then we have the names of the data in String format and
			// their specific resource ids
			HashMap<String, String> parkingLotsMap = new HashMap<String, String>();
			
			parkingLotsMap.put("vacantParkingLotId","2" );
			parkingLotsMap.put("lotsInfoTextView", "PSU");
			HashMap<String, String> parkingLotsMap2 = new HashMap<String, String>();
			
			parkingLotsMap2.put("vacantParkingLotId","1" );
			parkingLotsMap2.put("lotsInfoTextView", availableAddress);
			
			parkingLotsMapList.add(parkingLotsMap);
			parkingLotsMapList.add(parkingLotsMap2);*/
		
		
		/*Log.i("DisplayVacantParkingLots","before adapter");

		adapter = new SimpleAdapter( DisplayVacantParkingLots.this,parkingLotsMapList, R.layout.individual_vacant_parkinglots_info, new String[] { "vacantParkingLotId","lotsInfoTextView","distanceInMiles","costForParking"}, new int[] {R.id.vacantParkingLotId, R.id.lotsInfoTextView, R.id.distanceInMiles,R.id.costForParking});
		Log.i("DisplayVacantParkingLots","after adapter");

		setListAdapter(adapter);
		Log.i("DisplayVacantParkingLots","after setlistadapter");*/
		
	}

	
	@SuppressWarnings("deprecation")
	public void updateAdapter(ArrayList<HashMap<String, String>> arrayListMap, int success)
	{
		if (success == 1)
		{
		Log.i("DisplayVacantParkingLots","before adapter");

		adapter = new SimpleAdapter( DisplayVacantParkingLots.this,arrayListMap, R.layout.individual_vacant_parkinglots_info, new String[] { "vacantParkingLotId","lotsInfoTextView","distanceInMiles","costForParking"}, new int[] {R.id.vacantParkingLotId, R.id.lotsInfoTextView, R.id.distanceInMiles,R.id.costForParking});
		Log.i("DisplayVacantParkingLots","after adapter");

		setListAdapter(adapter);
		Log.i("DisplayVacantParkingLots","after setlistadapter");
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
		// TODO Auto-generated method stub
		super.onDestroy();
		
		//getParkingLotsFromDB.close();
	}
	
	class GetParkingLotsFromWebService extends AsyncTask<String, String, String>
	{
		DisplayVacantParkingLots dvpl;
		String latitude, longitude;
		String fromTime, toTime, radius;
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
        	this.blockedTimeInHours = ((toTime - fromTime)/3600000.0);
        	Log.i("DisplayVacantParkingLots:GetParkingLotsFromWebService","blockedTimeInHours" +blockedTimeInHours);
        	this.dvpl = dvpl;
        	}

		@Override
		protected String doInBackground(String... params) {
			ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
			postParams.add(new BasicNameValuePair("latitude",latitude ));
			postParams.add(new BasicNameValuePair("longitude", longitude));
			postParams.add(new BasicNameValuePair("radius", radius));
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
               			parkingLotsMap.put("distanceInMiles",(String.valueOf(costForparking)+ "$"));
               			
               			double dist = Double.parseDouble(json_data.getString("miles"));
               			parkingLotsMap.put("costForParking",(new DecimalFormat("##.##").format(dist) + "miles"));
               			
               			parkingLotsMapList.add(parkingLotsMap);
               			
            			// setListAdapter provides the Cursor for the ListView
            			// The Cursor provides access to the database data
            			
            			

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
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            dvpl.updateAdapter(parkingLotsMapList, success);
 
        }
	}


		

		
 
	
	
	

}
