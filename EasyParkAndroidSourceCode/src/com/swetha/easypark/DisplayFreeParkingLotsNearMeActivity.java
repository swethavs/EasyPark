package com.swetha.easypark;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.swetha.helpers.Constants;
import com.swetha.helpers.DateTimeHelpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DisplayFreeParkingLotsNearMeActivity extends ListActivity {
	private static String TAG = "DisplayFreeParkingLotsNearMeActivity";
	private static String TAG_FREEPARKINGLOTS = "freeparkinglots";
	private ProgressDialog pDialog;
	private static final String getFreeParkingLotsurl =  Constants.IPAddress +"/selectfreeparkinglots.php";
	private static ArrayList<HashMap<String, String>> freeparkingLotsMapList = new ArrayList<HashMap<String,String>>();
	private ListAdapter adapter;
	private int success;
	TextView tv_freeParkingLotsLatitude;
	TextView tv_freeParkingLotsLongitude;
	String fp_latitude, fp_longitude;
	

@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.displayfreeparkinglots);
	Intent intent = getIntent();
	double usersCurrentLatitude = intent.getDoubleExtra(GetParkingLots.LATITUDE,Constants.doubleDefaultValue); 
	double usersCurrentLongitude = intent.getDoubleExtra(GetParkingLots.LONGITUDE, Constants.doubleDefaultValue);
	 
	 Log.i(TAG, "DisplayFreeParkingLotsNearMe Activity called: After Oncreate");
	 
new GetFreeParkingLotsFromWebService(this, usersCurrentLatitude, usersCurrentLongitude).execute();

ListView listView = getListView();
listView.setOnItemClickListener(new OnItemClickListener() {
	
	public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
		
		// Get the hidden latitude and longitude from textview
		Log.i("DisplayParkingLotsAsList", "AfterOnItemClick");
		
		 tv_freeParkingLotsLatitude = (TextView) view.findViewById(R.id.tv_latitude);
		 tv_freeParkingLotsLongitude = (TextView) view.findViewById(R.id.tv_longitude);
	    fp_latitude = tv_freeParkingLotsLatitude.getText().toString();	
	    fp_longitude = tv_freeParkingLotsLongitude.getText().toString();
		
		Intent  theIndent = new Intent(DisplayFreeParkingLotsNearMeActivity.this, GoogleDirectionsActivity.class);
		theIndent.putExtra(GetParkingLots.LATITUDE, Double.valueOf(fp_latitude)); 
		theIndent.putExtra(GetParkingLots.LONGITUDE, Double.valueOf(fp_longitude));
		
		
		startActivity(theIndent); 
	}
}); 
}


class GetFreeParkingLotsFromWebService extends AsyncTask<String, String, String>
{
	DisplayFreeParkingLotsNearMeActivity dfvpl;
	String latitude,longitude;
	
	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(DisplayFreeParkingLotsNearMeActivity.this);
        pDialog.setMessage("Retreiving ParkingLots..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

	
	GetFreeParkingLotsFromWebService(DisplayFreeParkingLotsNearMeActivity dfvpl, double latitude, double longitude)
	{
		this.dfvpl = dfvpl;
		this.latitude =  String.valueOf(latitude);
    	this.longitude = String.valueOf(longitude);
    	}
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		ArrayList<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair("latitude",latitude ));
		postParams.add(new BasicNameValuePair("longitude", longitude));
		String response = null;
		 Log.i(TAG,"the postparams are" + postParams);
		 try {
		       response = EasyParkHttpClient.executeHttpPost(getFreeParkingLotsurl, postParams);
		       Log.i(TAG,"after making request jsonobject is" +response);
		       // store the result returned by PHP script that runs MySQL query
		       //String result = response.toString();  
		                
		        //parse json data
		           try{
		        	  String result = response.toString();
		                  String returnString = "";
		                  JSONObject jsonOb = new JSONObject(result);
		                  success = jsonOb.getInt(Constants.TAG_SUCCESS);
		                  if( success == 1)
		                  {
		             JSONArray jArray =  jsonOb.getJSONArray(TAG_FREEPARKINGLOTS);
		             Log.i(TAG,"after converting to obj" +jsonOb);

		                   for(int i=0;i<jArray.length();i++){
		                          JSONObject json_data = jArray.getJSONObject(i);
		                           Log.i("log_tag","hmFreePLNoOfSpots:"+json_data.getInt(Constants.hmFreePLNoOfSpots)+
		                                   ", hmFreePLUser: "+json_data.getString(Constants.hmFreePLUser)+
		                                   ", miles: "+json_data.getString(Constants.hmFreePLMiles)+
		                                   ",  hmFreePLlatitude:" +json_data.getString(Constants.hmFreePLlatitude)+
		                                   ",  hmFreePLlongitude:" +json_data.getString(Constants.hmFreePLlongitude)+
		                                   ",  hmFreePLtime:" +json_data.getString(Constants.hmFreePLTime)
		                           );
		                           //Get an output to the screen
		                           HashMap<String, String> freeparkingLotsMap = new HashMap<String, String>();
		               			
		                           freeparkingLotsMap.put(Constants.hmFreePLNoOfSpots, Constants.VACANTSPOTS +json_data.getString(Constants.hmFreePLNoOfSpots));
		                           freeparkingLotsMap.put(Constants.hmFreePLlatitude, json_data.getString(Constants.hmFreePLlatitude));
		                           freeparkingLotsMap.put(Constants.hmFreePLlongitude, json_data.getString(Constants.hmFreePLlongitude));
		               		       freeparkingLotsMap.put(Constants.hmFreePLAddress,json_data.getString(Constants.hmFreePLAddress));

		               		       //freeparkingLotsMap.put(Constants.hmFreePLUser,json_data.getString(Constants.hmFreePLUser)); - no longer needed 
		               		       double dist = Double.parseDouble(json_data.getString(Constants.hmFreePLMiles));
		               			   freeparkingLotsMap.put(Constants.hmFreePLMiles,(new DecimalFormat("##.##").format(dist) + "miles"));
		               			
		               		       long timeinms = Long.parseLong(json_data.getString(Constants.hmFreePLTime));
		               		       // combining the user name and updated time 
		               			 freeparkingLotsMap.put(Constants.hmFreePLTime, Constants.UPDATEDBY + " " +json_data.getString(Constants.hmFreePLUser)+ " "+ Constants.AT + " "+DateTimeHelpers.convertToTimeFromLong(timeinms));
		               			
		               			
		               			freeparkingLotsMapList.add(freeparkingLotsMap);
		            			
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
	
	protected void onPostExecute(String file_url) {
  
        pDialog.dismiss();
        //calling  UI thread to update UI
        dfvpl.updatefreeParkingLotsList(freeparkingLotsMapList, success);

    }
	
}


@SuppressWarnings("deprecation")
public void updatefreeParkingLotsList(ArrayList<HashMap<String, String>> freeparkingLotsMapList, int success)
{
	
		setListAdapter(null);
	
	// TODO Auto-generated method stub
	if (success == 1)
	{
		
	adapter = new SimpleAdapter( DisplayFreeParkingLotsNearMeActivity.this,freeparkingLotsMapList, R.layout.displayindividualfreeparkinglots, new String[] { Constants.hmFreePLAddress,Constants.hmFreePLNoOfSpots,Constants.hmFreePLMiles,Constants.hmFreePLTime, Constants.hmFreePLlatitude, Constants.hmFreePLlongitude}, new int[] {R.id.txtvw_displayfreelotsinfo, R.id.tv_numberOffreespots, R.id.noofmilesfp,R.id.tv_lastupdatedInfo, R.id.tv_latitude, R.id.tv_longitude});
	setListAdapter(adapter);
	
	}
	
		else
		{
			AlertDialog alertDialog = new AlertDialog.Builder(
                    DisplayFreeParkingLotsNearMeActivity.this).create();

   
    alertDialog.setTitle("Sorry!");

 
    alertDialog.setMessage("No parking lots found");



   
    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            // Write your code here to execute after dialog closed
            	Intent intent = new Intent(DisplayFreeParkingLotsNearMeActivity.this, GetParkingLots.class);
	            startActivity(intent);
            }
    });


    alertDialog.show();

		}

	}
@Override
public void onBackPressed()
{
    
    super.onBackPressed();
    BaseAdapter la = (BaseAdapter) getListAdapter();
    
    ListView lv = getListView();
    freeparkingLotsMapList.clear();// fixed the bug where duplicate data was getting added
    setListAdapter(null);
    la.notifyDataSetChanged();
    lv.clearTextFilter();
    
    finish();
	
    
}
/*@Override
protected void onResume()
{
    super.onResume();
    BaseAdapter la = (BaseAdapter) getListAdapter();
    la.notifyDataSetChanged();
    setListAdapter(null);
}*/

/*@Override
protected void onDestroy() {
	BaseAdapter la = (BaseAdapter) getListAdapter();
	freeparkingLotsMapList.clear();
	la.notifyDataSetChanged();
    super.onDestroy();
}*/
}
