/***************************************************************
Copyright (c) 2015 swethavs

[This program is licensed under the "MIT License"]

Please see the file COPYING in the source

distribution of this software for license terms.
****************************************************************/


package com.swetha.easypark;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class IndividualItemInListView extends LinearLayout {
	private static final String  TAG = "IndividualItemInListView";
	private TextView thisText;
	private TextView thisTextId;
	private RadioButton thisRadioBtn;
	private RadioButtonTracker thisTextSelected;
	public IndividualItemInListView(Context context, final RadioButtonTracker textSelected) {
		super(context);
		Log.i(TAG,"Inside IndividualItemInListView view constructor: textselected");
		
		
		this.setOrientation(HORIZONTAL);
		thisTextSelected = textSelected;
		// add image
		 ImageView myImage = new ImageView(context);
	        myImage.setImageResource(R.drawable.rsz_parkinglotsign);
	        addView(myImage, new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		//add  textview
	    thisText = new TextView(context);
		thisText.setPadding(15, 0, 0, 0);
		thisText.setTypeface(Typeface.SERIF, 0);
		Log.i(TAG,"Inside IndividualItemInListView view constructor after Typeface");
		thisText.setText(textSelected.getText());
		LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		params.gravity = Gravity.CENTER_VERTICAL;
		addView(thisText, params);
		
		
		
		thisTextId = new TextView(context);
		thisTextId.setPadding(15, 0, 0, 0);
		thisTextId.setVisibility(View.GONE);
		thisTextId.setText(textSelected.getId());
		params.weight = 2;
		addView(thisTextId, params);
		
		//add radio button
		thisRadioBtn = new RadioButton(context);
		thisRadioBtn.setChecked(textSelected.getSelected());
		thisRadioBtn.setFocusable(false);
		thisRadioBtn.setBackgroundColor(Color.GRAY);
		thisRadioBtn.setClickable(false);
		
		LinearLayout.LayoutParams checkBoxParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		checkBoxParams.gravity  = Gravity.CENTER_HORIZONTAL;
		
		addView(thisRadioBtn, params);
	}

	public void setText(String words) {
		thisText.setText(words);
	}
	
	public void setSelected(boolean selected) {
		thisRadioBtn.setChecked(selected);
		thisTextSelected.setSelected(selected);
	}
	
	public boolean getSelected() {
		return thisTextSelected.getSelected();
	}
}