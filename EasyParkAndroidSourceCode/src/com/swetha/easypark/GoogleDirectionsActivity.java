/***************************************************************
Copyright (c) 2015 swethavs

[This program is licensed under the "MIT License"]

Please see the file COPYING in the source

distribution of this software for license terms.
****************************************************************/

package com.swetha.easypark;

import java.util.ArrayList;

import org.w3c.dom.Document;

import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.swetha.helpers.Constants;

public class GoogleDirectionsActivity extends FragmentActivity implements LocationListener  {
	public  Location location = null;
	LocationManager locationManager;
	GoogleMap mMap;
    GetDirections md;
    String provider;
     double fromlat, fromlng;
     double tolat, tolng;
	LatLng fromPosition;
	LatLng toPosition;
	TextView tv_duration;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapdirections);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		  .detectDiskReads().detectDiskWrites().detectNetwork() // StrictMode is most commonly used to catch accidental disk or network access on the application's main thread
		  .penaltyLog().build());
        Log.i("GoogleDirectionsActivity","Inside Oncreate");
        
        Intent theIntent = getIntent();
        tolat = theIntent.getDoubleExtra(GetParkingLots.LATITUDE, Constants.doubleDefaultValue);
        tolng = theIntent.getDoubleExtra(GetParkingLots.LONGITUDE, Constants.doubleDefaultValue);
        
        toPosition = new LatLng(tolat, tolng);
         
        
        Log.i("GoogleDirectionsActivity","After Setting tolat, tolng, toPosition" +tolat + "\n"+tolng + "\n"+toPosition);
        
        md = new GetDirections();
        Log.i("GoogleDirectionsActivity","After calling GetDirctions constructor");
        
        FragmentManager fm =  getSupportFragmentManager();
        Log.i("GoogleDirectionsActivity","After creating fragmentManager" +fm);
        SupportMapFragment supportMapfragment = ((SupportMapFragment)fm.findFragmentById(R.id.drivingdirections));
        
        Log.i("GoogleDirectionsActivity","After creating SupportMapFragment" +supportMapfragment);
        mMap = supportMapfragment.getMap();
        Log.i("GoogleDirectionsActivity","After getting map");

        mMap.setMyLocationEnabled(true);
        Log.i("GoogleDirectionsActivity","After  setMyLocationEnabled");
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Log.i("GoogleDirectionsActivity","After  locationManager");
        Criteria criteria = new Criteria();
        
    
	   
	   // Getting the name of the provider that meets the criteria
        provider = locationManager.getBestProvider(criteria, true);
        
                
        if(provider!=null && !provider.equals("")){
        	
        	// Get the location from the given provider 
             location = locationManager.getLastKnownLocation(provider);
                        
            locationManager.requestLocationUpdates(provider, 500, 1, GoogleDirectionsActivity.this);
            
            
            if(location!=null)
            {
            	
            	fromlat = location.getLatitude();
            	fromlng = location.getLongitude();
            }
            	
            else
            {
            	fromlat = GetParkingLots.latitude;
            	fromlng = GetParkingLots.longitude;
            }
            
        }else{
        	Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();
        	finish();
        }
        Log.e("GoogleDirectionsActivity","After  setting location");
        fromPosition = new LatLng(fromlat, fromlng);
		LatLng coordinates = new LatLng(fromlat, fromlng);		
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 16));
		
		mMap.addMarker(new MarkerOptions().position(fromPosition).title("Start"));
		mMap.addMarker(new MarkerOptions().position(toPosition).title("End"));
		
		Document doc = md.getDocument(fromPosition, toPosition, Constants.MODE_DRIVING);
		String duration = md.getDurationText(doc);
		
		tv_duration = (TextView) findViewById(R.id.tv_time);
		tv_duration.setText("Estimated driving time:" +duration);
		
		ArrayList<LatLng> directionPoint = md.getDirection(doc);
		
		PolylineOptions rectLine = new PolylineOptions().width(6).color(Color.RED);
		
		for(int i = 0 ; i < directionPoint.size() ; i++) {			
			rectLine.add(directionPoint.get(i));
		}
		
		mMap.addPolyline(rectLine);
    }

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
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
	
	

}
