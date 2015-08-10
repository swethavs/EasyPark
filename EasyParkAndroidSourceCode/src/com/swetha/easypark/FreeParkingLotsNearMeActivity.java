package com.swetha.easypark;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

public class FreeParkingLotsNearMeActivity extends Activity {
	RadioGroup rg;
	RadioButton rb_insertFreeLots; 
	RadioButton rb_getFreeLots; 
	Button freeParkingLots_ok;
	Button freeParkingLots_cancel;
	int checkedRbId;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.i("FreeParkingLotsNearMeActivity", "Inside FreeParkingLotsNearMeActivity");
		
		setContentView(R.layout.freeparkingmainpage);
		
		rb_insertFreeLots = (RadioButton) findViewById(R.id.rbinsertfreeparkingspots);
		rb_getFreeLots = (RadioButton) findViewById(R.id.rbgetfreeparkingspots);
		
		freeParkingLots_ok = (Button) findViewById(R.id.okfreeparkinglots);
		
		freeParkingLots_ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rg = (RadioGroup) findViewById(R.id.rgfreeparkinglots);
			    checkedRbId = rg.getCheckedRadioButtonId();
			    
			    switch(checkedRbId)
			    {
			    	case R.id.rbgetfreeparkingspots:
			    		Intent getFreeParkingSpotsIntent = new Intent(FreeParkingLotsNearMeActivity.this,DisplayFreeParkingLotsNearMeActivity.class );
			    		getFreeParkingSpotsIntent.putExtra(GetParkingLots.LATITUDE, GetParkingLots.latitude);
			    		getFreeParkingSpotsIntent.putExtra(GetParkingLots.LONGITUDE, GetParkingLots.longitude);
			    		startActivity(getFreeParkingSpotsIntent);
			    		break;
			    	case R.id.rbinsertfreeparkingspots:
			    		Intent InsertFreeParkingSpotsIntent = new Intent(FreeParkingLotsNearMeActivity.this,KeyInFreeParkingSpotValues.class );
			    		startActivity(InsertFreeParkingSpotsIntent);
			    		break;
			    }
				
			}
		});
		
		freeParkingLots_cancel = (Button) findViewById(R.id.cancel_free_parkinglots);
		
		freeParkingLots_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FreeParkingLotsNearMeActivity.this, GetParkingLots.class);
				startActivity(intent);
			}
		});
		
		
		
	}
	

}
