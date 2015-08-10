package com.swetha.easypark;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * @author Swetha Venkatachari Sundarajan
 * This class displays the available parking lots in the form of list including the distance 
 * from the current location and the cost for parking
 *
 */

public class DisplayParkingLotsAsList extends ListActivity {
	
	TextView vacntParkingLotId;
	ListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.displayvacantparkinglots);
		
		ArrayList<HashMap<String, String>> arl = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra(DisplayVacantParkingLots.ARRAYLISTMAP);
		Log.i("DisplayParkingLotsAsList",""+arl.get(0).get("latitude"));
		adapter = new SimpleAdapter( DisplayParkingLotsAsList.this,arl, R.layout.individual_vacant_parkinglots_info, new String[] { "vacantParkingLotId","lotsInfoTextView","distanceInMiles","costForParking"}, new int[] {R.id.vacantParkingLotId, R.id.lotsInfoTextView, R.id.distanceInMiles,R.id.costForParking});
		Log.i("DisplayParkingLotsAsList","after adapter");

		setListAdapter(adapter);

		ListView listView = getListView();
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				
				// Get the hidden parkinglotid from textview
				Log.i("DisplayParkingLotsAsList", "AfterOnItemClick");
				
				vacntParkingLotId = (TextView) view.findViewById(R.id.vacantParkingLotId);
			    String vacntParkingLotIdValue = vacntParkingLotId.getText().toString();	
				
				Intent  theIndent = new Intent(DisplayParkingLotsAsList.this, GetIndividualParkingSpotDetails.class);
				theIndent.putExtra("individualParkingLotId", vacntParkingLotIdValue); 
				theIndent.putExtra(GetParkingLots.FROMTIME, DisplayVacantParkingLots.fromTime);
				theIndent.putExtra(GetParkingLots.TOTIME, DisplayVacantParkingLots.toTime);
				
				
				startActivity(theIndent); 
			}
		}); 
	}
	}
