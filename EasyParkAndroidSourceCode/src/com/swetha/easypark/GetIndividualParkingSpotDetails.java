/***************************************************************
Copyright (c) 2015 swethavs

[This program is licensed under the "MIT License"]

Please see the file COPYING in the source

distribution of this software for license terms.
****************************************************************/


package com.swetha.easypark;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.swetha.helpers.DateTimeHelpers;
import com.swetha.helpers.Constants;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;



@SuppressWarnings({ "deprecation" })
public class GetIndividualParkingSpotDetails extends ListActivity {
	
	private List<RadioButtonTracker> mSelectionList = new ArrayList<RadioButtonTracker>();
	private Context mContext;
	private Button btn_next, btn_back;
	private CheckBox chk_default;
	private ArrayList<LinkedHashMap<String, String>> parkingSpotMapList = new ArrayList<LinkedHashMap<String, String>>();
	private int mSelectedItem = 0;
	private String TAG_PARKINGSPOTS = "parkingspots";
	private String TAG_UPDATEPARKINGSPOT = "parkingaddressarray";
	private String TAG_SUCCESS = "success";
	//static final String getParkingSpotsurl = "http://easypark.net46.net/easypark/getparkingspots.php";
	//static final String updateParkingSpoturl = "http://easypark.net46.net/easypark/updateparkingspot.php";
	static final String getParkingSpotsurl =  Constants.IPAddress+ "/getparkingspots.php";
	static final String updateParkingSpoturl =  Constants.IPAddress +"/updateparkingspot.php";
	 
	 
	 private ProgressDialog pDialog;
	 String parkingLotId;
	 double latitude, longitude;
	 String address;
	 String theParkingSpotName;
	 String theParkingSpotId;
	long toTime, fromTime;
	int success;
	RadioButtonTrackerListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("GetIndividualParkingSpotDetails","Before Set ContentView");
		 mContext = this.getApplicationContext();
		    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    
		setContentView(R.layout.listvacantparkingspots);
		Log.i("GetIndividualParkingSpotDetails","After Set ContentView");
		
		Intent thisIntent = getIntent();
		 parkingLotId = thisIntent.getStringExtra("individualParkingLotId");
		 fromTime = thisIntent.getLongExtra(GetParkingLots.FROMTIME, 0);
		toTime = thisIntent.getLongExtra(GetParkingLots.TOTIME, 0);
		
		Log.i("GetIndividualParkingSpotDetails","After getting from intent" +fromTime +toTime +parkingLotId);
	
	/*	Test Data
	 * parkingSpotMapList = new ArrayList<LinkedHashMap<String, String>>();
		LinkedHashMap<String, String> parkingSpotMap = new LinkedHashMap<String, String>();
		
		parkingSpotMap.put("parkingspotid", parkingLotId);
		Log.i("GetIndividualParkingSpotDetails", "PArking lot id" +parkingLotId);
		parkingSpotMap.put("vacantspotdisplay", "A5");
		LinkedHashMap<String, String> parkingSpotMap2 = new LinkedHashMap<String, String>();
		
		parkingSpotMap2.put("parkingspotid", "2");
		parkingSpotMap2.put("vacantspotdisplay", "A6");
		
		parkingSpotMapList.add(parkingSpotMap);
		parkingSpotMapList.add(parkingSpotMap2);*/
		
		new GetParkingSpotsFromWebService(this, fromTime, toTime ).execute();
		Log.i("GetIndividualParkingSpotDetails","After adding to hashmap list");
		
	
	        btn_next = (Button) findViewById(R.id.single_selection_btn_first);
		    btn_next.setClickable(true);
		    btn_next.setFocusable(true);
		    Log.i("GetIndividualParkingSpotDetails","After firstbutton focussable");
		    btn_next.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					
					//ListView l = getListView();
			    	//TextSelectedView l_v = (TextSelectedView)v;
 
			    	HashMap<String, String> hmap = parkingSpotMapList.get(mSelectedItem);
			    	Log.i("GetIndividualParkingSpotDetails","The value of mSelectedItem:" +mSelectedItem);
			    	 theParkingSpotName= hmap.get("vacantspotdisplay");
			    	theParkingSpotId= hmap.get("parkingspotid");
			    	
			    	
			    	Log.i("GetIndividualParkingSpotDetails","The selected parking spot  is " +theParkingSpotName);
			    	Log.i("GetIndividualParkingSpotDetails","The selected parking spotid  is " +theParkingSpotId);
			    	
			    	chk_default = (CheckBox) findViewById(R.id.single_selection_chkbox_default);
			    	new UpdateParkingSpotThroughWebService(GetIndividualParkingSpotDetails.this, theParkingSpotId, fromTime, toTime).execute();
			    	
			        		
				}
			});
		    Log.i("GetIndividualParkingSpotDetails","After settOnClickListener first button");
		    btn_back = (Button) findViewById(R.id.single_selection_btn_second);
		    btn_back.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					//close the activity
					finish();
				}
			});
		    Log.i("GetIndividualParkingSpotDetails","After settOnClickListener second button");
		    
		    chk_default = (CheckBox) findViewById(R.id.single_selection_chkbox_default);
		    chk_default.setChecked(false);
		    Log.i("GetIndividualParkingSpotDetails","After checkbox");
		    
		    //tv_ins = (TextView) this.findViewById(R.id.single_selection_chkbox_default_text);
		    //tv_ins.setText("Test");
		    Log.i("GetIndividualParkingSpotDetails","After textView end of oncreate");
		}
		
		@Override
	    protected void onListItemClick(ListView l, View v, int position, long id) 
	    {    
			RadioButtonTracker l_prevV;
			Log.i("GetIndividualParkingSpotDetails","Inside on list Itemclick");
	    	IndividualItemInListView l_v = (IndividualItemInListView)v;
	    	Log.i("GetIndividualParkingSpotDetails","Inside onlistItemclick after IndividualItemInListView" +l_v);
	    	if (!l_v.getSelected())  {
	    		Log.i("GetIndividualParkingSpotDetails", "value of list is" +l);
	    		ListView lv = getListView();
	    		Log.i("GetIndividualParkingSpotDetails", "value of mselectedItem" +mSelectedItem);
	    		Log.i("GetIndividualParkingSpotDetails", "Value of l_prevV" +l.getItemAtPosition(mSelectedItem));
	    		if (l.getItemAtPosition(mSelectedItem) == null)
	    		{
	    		//change the prev selected to unselected
	    		 //l_prevV = (TextSelectedView)lv.getChildAt(mSelectedItem);
	    		//l_prevV.setSelected(false);
	    		}
	    		else
	    		{
	    			l_prevV = (RadioButtonTracker)l.getItemAtPosition(mSelectedItem);
		    		l_prevV.setSelected(false);
	    		}
	    		mSelectedItem = position;
	    		l_v.setSelected(true);
	    	}
	    	adapter.notifyDataSetChanged();
	    	
	    }
		
		
		public void updateListView(ArrayList<LinkedHashMap<String, String>> parkingSpotMapList, int success)
		{
			if (success == 1)
			{
		 mSelectedItem = 0;	    
		 for (int i=0; i< parkingSpotMapList.size(); i++) {
			 Log.i("GetIndividualParkingSpotDetails","Inside For loop");
			 RadioButtonTracker l_ts;
			 LinkedHashMap<String, String>  map = parkingSpotMapList.get(i);
			 Log.i("GetIndividualParkingSpotDetails","Value in map is "+ map.get("vacantspotdisplay"));
			 Log.i("GetIndividualParkingSpotDetails","parking spoid is is "+ map.get("parkingspotid"));
			 if(i == mSelectedItem)
			 {
				 Log.i("GetIndividualParkingSpotDetails","Before calling RadioButtonTracker constructor - if");
	    		l_ts = new RadioButtonTracker(map, true);
	    		Log.i("GetIndividualParkingSpotDetails","After calling RadioButtonTracker constructor-if");
	    		} else {
	    			l_ts = new RadioButtonTracker(map, false);
	    		}
		    	mSelectionList.add(l_ts);
	    	}
		 Log.i("GetIndividualParkingSpotDetails","After -if-else condition");
		 adapter  = new RadioButtonTrackerListAdapter(mContext);
	    	adapter.setListItems(mSelectionList);
	    	Log.i("GetIndividualParkingSpotDetails","After setListItems");
	        this.setListAdapter(adapter);
	        Log.i("GetIndividualParkingSpotDetails","After setListAdapter");
		}
			else 
			{
				AlertDialog alertDialog = new AlertDialog.Builder(
                        GetIndividualParkingSpotDetails.this).create();
 
        // Setting Dialog Title
        alertDialog.setTitle("Sorry!");
 
        // Setting Dialog Message
        alertDialog.setMessage("No Parking spots found");
 
        // Setting Icon to Dialog
       // alertDialog.setIcon(R.drawable.tick);
 
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                	Intent intent = new Intent(GetIndividualParkingSpotDetails.this, GetParkingLots.class);
		            startActivity(intent);
                }
        });
 
        // Showing Alert Message
        alertDialog.show();
 
			}
		}
		
		
		  private void scheduleNotification(Notification notification) {
		    	
		        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
		        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, Integer.parseInt(theParkingSpotId));
		        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
		        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		        
		        long currentTimeInLong = DateTimeHelpers.convertToLongFromTime(DateTimeHelpers.dtf.format(new Date()).toString());
		        long delay = toTime - (currentTimeInLong + Constants.tenMinutesInMilliseconds);
		        long futureInMills = SystemClock.elapsedRealtime() + delay;
		        
		        Log.i("GetIndividualParkingSpotDetails"," Spot has been blocked till long " +toTime);
		    	Log.i("GetIndividualParkingSpotDetails"," Spot has been blocked till  DateTime" +DateTimeHelpers.convertToTimeFromLong(toTime));
		    	
		    	Log.i("GetIndividualParkingSpotDetails"," CurrentTime in  long " +currentTimeInLong);
		    	Log.i("GetIndividualParkingSpotDetails"," Spot has been blocked till  DateTime" +DateTimeHelpers.convertToTimeFromLong(currentTimeInLong));
		    	
		    	Log.i("GetIndividualParkingSpotDetails", "value of delay" +delay);
		    	Log.i("GetIndividualParkingSpotDetails", "value of mills in time"+DateTimeHelpers.convertToTimeFromLong(futureInMills));
		    	 
		    	
		    	
		    	
		        Log.i("GetIndividualParkingSpotDetails", "Notification is kept at this mills" +futureInMills);
		        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMills, pendingIntent);
		        
		        
		    }
		  
		  private Notification getNotification(String content) {
		    	Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		        Notification.Builder builder = new Notification.Builder(this);
		        builder.setContentTitle("EasyPark Notification");
		        builder.setContentText(content);
		        builder.setSmallIcon(R.drawable.ic_car);
		        builder.setPriority(BIND_IMPORTANT);
		        builder.setSound(soundUri);
		        
		       // return builder.build();
		        
		        return new Notification.BigTextStyle(builder)
		        .bigText(content).build();
		    }
		  
		 
		public void  displayalertdialog(double latitude, double longitude, String address, int success)
		 {
			 final double lat = latitude;
			 final double lng = longitude;
			 if (success == 1)
			 {
		    	final AlertDialog alertDialog = new AlertDialog.Builder(
                        GetIndividualParkingSpotDetails.this).create();
 
        // Setting Dialog Title
        alertDialog.setTitle("Congratulations!");
 
        // Setting Dialog Message
        alertDialog.setMessage("Your parking spot has been blocked");
 
        // Setting Icon to Dialog
       // alertDialog.setIcon(R.drawable.tick);
 
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	
                	alertDialog.dismiss();
                	AlertDialog alertDialog1 = new AlertDialog.Builder(
                            GetIndividualParkingSpotDetails.this).create();
     
            // Setting Dialog Title
            alertDialog1.setTitle("Do you want to get directions?");
            alertDialog1.setButton2("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Log.e("GetIndividualParkingSpots","Inside Yes");
					Intent yesintent = new Intent(GetIndividualParkingSpotDetails.this, GoogleDirectionsActivity.class);
					yesintent.putExtra(GetParkingLots.LATITUDE, lat);
					yesintent.putExtra(GetParkingLots.LONGITUDE, lng);
					
					startActivity(yesintent);
					Log.e("GetIndividualParkingSpots","After Calling Intent");
				}
			});
            
            alertDialog1.setButton3("No", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
		            // Write your code here to execute after dialog closed
	                	Intent nointent = new Intent(GetIndividualParkingSpotDetails.this, GetParkingLots.class);
			            startActivity(nointent);
					
				}
			});
            alertDialog1.show();
     
            
                }
        });
 
        // Showing Alert Message
        alertDialog.show();
        if(chk_default.isChecked())
    	{
	    	Log.i("GetIndividualParkingSpotDetails","Inside Checkbox if ");
	    	Log.i("GetIndividualParkingSpotDetails"," Spot has been blocked till long " +toTime);
	    	Log.i("GetIndividualParkingSpotDetails"," Spot has been blocked till  DateTime" +DateTimeHelpers.convertToTimeFromLong(toTime));
	    	
	    	Log.i("GetIndividualParkingSpotDetails","Inside if checkbox address" +address );
	    	scheduleNotification(getNotification("You parked the car in" +address + "Your parking spot id is "+theParkingSpotName));

    	}
			 }
			 else
			 {
			    	AlertDialog alertDialog = new AlertDialog.Builder(
	                        GetIndividualParkingSpotDetails.this).create();
	 
	        // Setting Dialog Title
	        alertDialog.setTitle("Sorry!");
	 
	        // Setting Dialog Message
	        alertDialog.setMessage("There was a problem processing your request");
	 
	        // Setting Icon to Dialog
	       // alertDialog.setIcon(R.drawable.tick);
	 
	        // Setting OK Button
	        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                // Write your code here to execute after dialog closed
	                	Intent intent = new Intent(GetIndividualParkingSpotDetails.this, GetParkingLots.class);
			            startActivity(intent);
	                }
	        });
	 
	        // Showing Alert Message
	        alertDialog.show();
			 }
         
		 }
		  
		  
		  class GetParkingSpotsFromWebService extends AsyncTask<String, String, String>
			{
				GetIndividualParkingSpotDetails gipsd;;
				
				String parkinglotsid, fromTime, toTime;
				 /**
		         * Before starting background thread Show Progress Dialog
		         * */
		        @Override
		        protected void onPreExecute() {
		            super.onPreExecute();
		            pDialog = new ProgressDialog(GetIndividualParkingSpotDetails.this);
		            pDialog.setMessage("Retreiving ParkingSpots..");
		            pDialog.setIndeterminate(false);
		            pDialog.setCancelable(true);
		            pDialog.show();
		        }
		        
		        GetParkingSpotsFromWebService(GetIndividualParkingSpotDetails gips, long fromTime, long toTime)
		        {
		        	
		        	
		        	this.fromTime = String.valueOf(fromTime);
		        	this.toTime = String.valueOf(toTime);
		        	this.parkinglotsid = String.valueOf(GetIndividualParkingSpotDetails.this.parkingLotId);
		        	this.gipsd = gips;
		        	}

				
				@Override
				protected String doInBackground(String... params) {
					ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
					postParams.add(new BasicNameValuePair("individualparkinglotid",parkinglotsid ));
					postParams.add(new BasicNameValuePair("fromtime", fromTime));
					postParams.add(new BasicNameValuePair("endtime", toTime));
		            
		            String response = null;
		            Log.i("GetIndividualParkingSpotDetails:GetParkingSpotsFromWebService","the postparams are" + postParams);

		            // call executeHttpPost method passing necessary parameters 
		            try {
		       response = EasyParkHttpClient.executeHttpPost(getParkingSpotsurl, postParams);
		       Log.i("GetIndividualParkingSpotDetails:GetParkingSpotsFromWebService","after making request jsonobject is" +response);
		       // store the result returned by PHP script that runs MySQL query
		       //String result = response.toString();  
		                
		        //parse json data
		           try{
		        	  String result = response.toString();
		                  
		                  JSONObject jsonOb = new JSONObject(result);
		                  success = jsonOb.getInt(TAG_SUCCESS);
		                  if (success == 1)
		                  {
		             JSONArray jArray =  jsonOb.getJSONArray(TAG_PARKINGSPOTS);
		             Log.i("GetIndividualParkingSpotDetails:GetParkingSpotsFromWebService","after converting to obj" +jsonOb);
		             
		                   for(int i=0;i<jArray.length();i++){
		                          JSONObject json_data = jArray.getJSONObject(i);
		                          Log.i("log_tag","parkingspotsid: "+json_data.getInt("parkingspotsid")+
		                                   ", parkingspotname: "+json_data.getString("parkingspotname")
		                                   );
		                           //Get an output to the screen
		                           LinkedHashMap<String, String> parkingSpotMap = new LinkedHashMap<String, String>();
		               			
		                           parkingSpotMap.put("parkingspotid",json_data.getString("parkingspotsid"));
		                           parkingSpotMap.put("vacantspotdisplay", json_data.getString("parkingspotname"));
		                           Log.i("GetIndividualParkingSpotDetails:GetParkingSpotsFromWebService","after converting to obj" +parkingSpotMap);
		               			parkingSpotMapList.add(parkingSpotMap);
		               			
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
		            gipsd.updateListView(parkingSpotMapList, success);
		            Log.i("GetIndividualParkingSpotDetails:GetParkingSpotsFromWebService","inside postExecute value of list" +parkingSpotMapList);
		 
		        }
			}


		  
		  class UpdateParkingSpotThroughWebService extends AsyncTask<String, String, String>
			{
				GetIndividualParkingSpotDetails gipsd;;
				
				String parkinglotsid, parkingSpotId, fromTime, toTime;
				double latitude, longitude;
				String address;
				 /**
		         * Before starting background thread Show Progress Dialog
		         * */
		        @Override
		        protected void onPreExecute() {
		            super.onPreExecute();
		            pDialog = new ProgressDialog(GetIndividualParkingSpotDetails.this);
		            pDialog.setMessage("Updating ParkingSpot..");
		            pDialog.setIndeterminate(false);
		            pDialog.setCancelable(true);
		            pDialog.show();
		        }
		        
		        UpdateParkingSpotThroughWebService(GetIndividualParkingSpotDetails gips, String parkingSpotid, long fromTime, long toTime)
		        {
		        	
		        	
		        	this.fromTime = String.valueOf(fromTime);
		        	this.toTime = String.valueOf(toTime);
		        	this.parkinglotsid = String.valueOf(GetIndividualParkingSpotDetails.this.parkingLotId);
		        	this.parkingSpotId = parkingSpotid;
		        	this.gipsd = gips;
		        	}

				
				@Override
				protected String doInBackground(String... params) {
					ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
					postParams.add(new BasicNameValuePair("individualparkinglotid",parkinglotsid ));
					postParams.add(new BasicNameValuePair("fromtime", fromTime));
					postParams.add(new BasicNameValuePair("endtime", toTime));
					postParams.add(new BasicNameValuePair("parkingspotid", parkingSpotId));
		            
		            String response = null;
		            Log.i("GetIndividualParkingSpotDetails:UpdateParkingSpotThroughWebService","the postparams are" + postParams);

		            // call executeHttpPost method passing necessary parameters 
		            try {
		       response = EasyParkHttpClient.executeHttpPost(updateParkingSpoturl, postParams);
		       Log.i("GetIndividualParkingSpotDetails:UpdateParkingSpotThroughWebService","after making request jsonobject is" +response);
		       // store the result returned by PHP script that runs MySQL query
		       //String result = response.toString();  
		                
		        //parse json data
		           try{
		        	  String result = response.toString();
		                  success = 0;
		                  JSONObject jsonOb = new JSONObject(result);
		                  success = jsonOb.getInt(TAG_SUCCESS);
		                  if (success == 1)
		                  {
		             JSONArray jArray =  jsonOb.getJSONArray(TAG_UPDATEPARKINGSPOT);
		             Log.i("GetIndividualParkingSpotDetails:UpdateParkingSpotThroughWebService","after converting to obj" +jsonOb);

		                   for(int i=0;i<jArray.length();i++){
		                          JSONObject json_data = jArray.getJSONObject(i);
		                         
		                           Log.i("log_tag","latitude: "+json_data.getDouble("latitude")+
			                                  "longitude:"+json_data.getString("longitude") +
			                                  "parkingaddress:" +json_data.getString("parkingaddress")
			                                  );
		                           //Get an output to the screen
		                           
		               				latitude = json_data.getDouble("latitude");
		               				longitude = json_data.getDouble("longitude");
		               				address = json_data.getString("parkingaddress");
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
		            gipsd.displayalertdialog(latitude, longitude, address, success);
		            Log.i("GetIndividualParkingSpotDetails:UpdateParkingSpotsFromWebService","inside postExecute value of list" +parkingSpotMapList);
		 
		        }
			}
		  }
